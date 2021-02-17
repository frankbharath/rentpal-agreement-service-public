package com.rentpal.agreement.controller;

import com.rentpal.agreement.common.Constants;
import com.rentpal.agreement.common.DTOModelMapper;
import com.rentpal.agreement.common.Utils;
import com.rentpal.agreement.dto.PropertyDTO;
import com.rentpal.agreement.dto.TenantDTO;
import com.rentpal.agreement.model.*;
import com.rentpal.agreement.service.interfaces.PropertyService;
import com.rentpal.agreement.service.interfaces.TenantService;
import com.rentpal.agreement.validator.PropertyValidator;
import com.rentpal.agreement.validator.UnitValidator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author frank
 * @created 15 Dec,2020 - 12:11 AM
 * Controller that performs CRUD operation on property and its units.
 */

@RestController
public class PropertyController {

    /**
     * PropertyService that holds business logic for performing CRUD operations
     */
    private final PropertyService propertyService;



    /**
    * Validates the property object
    */
    private final PropertyValidator propertyValidator;

    /**
     * Validates the unit object
     */
    private final UnitValidator unitValidator;

    /**
     * Instantiates a new Property controller.
     *
     * @param propertyService   the property service
     * @param propertyValidator the property validator
     * @param unitValidator     the unit validator
     */
    public PropertyController(PropertyService propertyService, PropertyValidator propertyValidator, UnitValidator unitValidator){
        this.propertyService=propertyService;
        this.propertyValidator=propertyValidator;
        this.unitValidator=unitValidator;
    }

    /**
     * Gets all the properties from the database.
     *
     * @param searchQuery the search query
     * @return the response entity
     */
    @GetMapping(value="/properties", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getProperties(@RequestParam Optional<String> searchQuery, @RequestParam(defaultValue = Constants.DEFAULT_START_PAGE) Integer pageIndex,
                                                @RequestParam Optional<Integer> pageSize, @RequestParam(defaultValue = "false") Boolean countRequired){
        List<PropertyDTO> propertyDTOS=DTOModelMapper.propertiesModelDTOMapper(propertyService.getProperties(searchQuery.orElse(null), pageIndex, pageSize.orElse(null)));
        HttpHeaders responseHeaders = new HttpHeaders();
        if(countRequired){
            responseHeaders.set("X-Total-Count", propertyService.getPropertiesCount(searchQuery.orElse(null)).toString());
        }
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(propertyDTOS);
    }

    /**
     * Gets a single property for a given id.
     *
     * @param id the id
     * @return the response entity
     */
    @GetMapping(value = "/properties/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getProperty(@PathVariable Long id){
        return new ResponseEntity<>(DTOModelMapper.propertyModelDTOMapper(propertyService.getProperty(id)), HttpStatus.OK);
    }

    /**
     * Adds property to the database.
     *
     * @param propertyDTO     the property
     * @param bindingResult the binding result
     * @return the response entity
     * @throws BindException the bind exception
     */
    @PostMapping(value="/properties", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addProperty(@RequestBody PropertyDTO propertyDTO, BindingResult bindingResult) throws BindException {
        Property property=DTOModelMapper.propertyDTOModelMapper(propertyDTO);
        propertyValidator.validate(propertyDTO, bindingResult);
        if(bindingResult.hasErrors()){
            throw new BindException(bindingResult);
        }
        return new ResponseEntity<>(DTOModelMapper.propertyModelDTOMapper(propertyService.addProperty(property)), HttpStatus.OK);
    }

    /**
     * Updates property to the database.
     *
     * @param id            the id
     * @param propertyDTO      the property
     * @param bindingResult the binding result
     * @return the response entity
     * @throws BindException the bind exception
     */
    @PutMapping(value= "/properties/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateProperty(@PathVariable Long id, @RequestBody PropertyDTO propertyDTO, BindingResult bindingResult) throws BindException {
        Property property=DTOModelMapper.propertyDTOModelMapper(propertyDTO);
        propertyValidator.validate(propertyDTO, bindingResult);
        if(bindingResult.hasErrors()){
            throw new BindException(bindingResult);
        }
        property.setId(id);
        return new ResponseEntity<>(DTOModelMapper.propertyModelDTOMapper(propertyService.updateProperty(property)), HttpStatus.OK);
    }

    /**
     * Delete a property from the database for given id.
     *
     * @param id the id
     * @return the response entity
     */
    @DeleteMapping(value = "/properties/{id}")
    public ResponseEntity<Void>  deleteProperty(@PathVariable Long id){
        propertyService.deleteProperty(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value="/properties/bulkdelete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void>  deleteProperties(@RequestParam Optional<List<Long>> propertyIds){
        propertyService.deleteProperties(propertyIds.orElse(new ArrayList<>()));
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    /*@PostMapping(value = "/properties/{id}/units/{unitId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addTenant(@PathVariable Long id, @PathVariable Long unitId, @RequestBody TenantDTO tenantDTO){
        return new ResponseEntity<>(Utils.getApiRequestResponse(tenantService.addTenant(id, unitId, tenantDTO)), HttpStatus.OK);
    }*/
}
