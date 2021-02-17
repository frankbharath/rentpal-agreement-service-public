package com.rentpal.agreement.controller;

import com.rentpal.agreement.common.Constants;
import com.rentpal.agreement.common.DTOModelMapper;
import com.rentpal.agreement.dto.TenantDTO;
import com.rentpal.agreement.model.Tenant;
import com.rentpal.agreement.service.interfaces.TenantService;
import com.rentpal.agreement.validator.TenantValidator;
import org.json.JSONObject;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author frank
 * @created 03 Feb,2021 - 12:40 PM
 */

@RestController
public class TenantController {

    private final TenantService tenantService;

    private final TenantValidator tenantValidator;

    /**
     * For localization, I18N
     */
    private final MessageSource messageSource;

    public TenantController(TenantService tenantService, TenantValidator tenantValidator, MessageSource messageSource) {
        this.tenantService = tenantService;
        this.tenantValidator=tenantValidator;
        this.messageSource=messageSource;
    }

    @PostMapping(value = "/tenants", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addTenant(@RequestBody TenantDTO tenantDTO, BindingResult bindingResult) throws BindException {
        tenantValidator.validate(tenantDTO, bindingResult);
        if(bindingResult.hasErrors()){
            throw new BindException(bindingResult);
        }
        return new ResponseEntity<>(tenantService.addTenant(tenantDTO), HttpStatus.OK);
    }

    @GetMapping(value = "/tenants")
    public ResponseEntity<Object> getTenants(@RequestParam(defaultValue = Constants.DEFAULT_START_PAGE) Integer pageIndex,
                                             @RequestParam Optional<Integer> pageSize, @RequestParam(defaultValue = "false") Boolean countRequired){
        HttpHeaders responseHeaders = new HttpHeaders();
        if(countRequired){
            responseHeaders.set("X-Total-Count", tenantService.getAllTenantsCount().toString());
        }
        List<Tenant> tenants=tenantService.getAllTenants(pageIndex, pageSize.orElse(null));
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(DTOModelMapper.tenantModelDTOMappers(tenants, messageSource));
    }

    @GetMapping(value = "/tenants/summary/rent")
    public ResponseEntity<Object> getTenantRentSummary(){
        return ResponseEntity.ok().body(tenantService.getRentHistory().toString());
    }

    @DeleteMapping(value = "/tenants/{id}")
    public ResponseEntity<Void>  deleteTenant(@PathVariable Long id){
        tenantService.deleteTenant(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
