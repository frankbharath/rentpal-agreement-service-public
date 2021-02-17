package com.rentpal.agreement.controller;

import com.rentpal.agreement.common.Constants;
import com.rentpal.agreement.common.DTOModelMapper;
import com.rentpal.agreement.common.Utils;
import com.rentpal.agreement.dto.UnitDTO;
import com.rentpal.agreement.model.Unit;
import com.rentpal.agreement.service.interfaces.UnitService;
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
 * @created 23 Dec,2020 - 9:09 PM
 */
@RestController
public class UnitController {

    private final UnitService unitService;

    /**
     * Validates the unit object
     */
    private final UnitValidator unitValidator;

    public UnitController(UnitService unitService, UnitValidator unitValidator){
        this.unitService=unitService;
        this.unitValidator=unitValidator;
    }

    /**
     * Gets units for given property.
     *
     * @param id the id
     * @return the response entity
     */
    @GetMapping(value = "/properties/{id}/units", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUnitsForProperty(@PathVariable Long id, @RequestParam(defaultValue = Constants.DEFAULT_START_PAGE) Integer pageIndex,
                                                      @RequestParam Optional<Integer> pageSize, @RequestParam(defaultValue = "false") Boolean countRequired){
        HttpHeaders responseHeaders = new HttpHeaders();
        if(countRequired){
            responseHeaders.set("X-Total-Count", unitService.getUnitCount(id).toString());
        }
        List<UnitDTO> unitDTOS=DTOModelMapper.unitsModelDTOMapper(unitService.getUnitsForProperty(id, pageIndex, pageSize.orElse(null)));
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(unitDTOS);
    }

    /**
     * Gets a unit for given and property.
     *
     * @param id     the id
     * @param unitId the unit id
     * @return the response entity
     */
    @GetMapping(value = "/properties/{id}/units/{unitId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUnitForProperty(@PathVariable Long id, @PathVariable Long unitId){
        return new ResponseEntity<>(DTOModelMapper.unitModelDTOMapper(unitService.getUnitForProperty(id, unitId)), HttpStatus.OK);
    }

    /**
     * Adds a unit to the property.
     *
     * @param id            the id
     * @param unit          the unit
     * @param bindingResult the binding result
     * @return the response entity
     * @throws BindException the bind exception
     */
    @PostMapping(value = "/properties/{id}/units", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addUnitToProperty(@PathVariable Long id, @RequestBody Unit unit, BindingResult bindingResult) throws BindException {
        unitValidator.validate(unit, bindingResult);
        if(bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        return new ResponseEntity<>(DTOModelMapper.unitModelDTOMapper(unitService.addUnitToProperty(id, unit)), HttpStatus.OK);
    }

    /**
     * Updates a unit to the given property.
     *
     * @param id            the id
     * @param unitId        the unit id
     * @param unit          the unit
     * @param bindingResult the binding result
     * @return the response entity
     * @throws BindException the bind exception
     */
    @PutMapping(value = "/properties/{id}/units/{unitId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateUnitToProperty(@PathVariable Long id, @PathVariable Long unitId, @RequestBody Unit unit,  BindingResult bindingResult) throws BindException {
        unit.setId(unitId);
        unitValidator.validate(unit, bindingResult);
        if(bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        return new ResponseEntity<>(DTOModelMapper.unitModelDTOMapper(unitService.updateUnitToProperty(id, unit)), HttpStatus.OK);
    }

    /**
     * Deletes unit for given id and property.
     *
     * @param id     the id
     * @param unitId the unit id
     * @return the response entity
     */
    @DeleteMapping(value = "/properties/{id}/units/{unitId}")
    public ResponseEntity<Void>  deleteUnitForProperty(@PathVariable Long id, @PathVariable Long unitId){
        unitService.deleteUnitForProperty(id, unitId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/properties/{id}/units/bulkdelete")
    public ResponseEntity<Void> deleteUnits(@PathVariable Long id, @RequestParam Optional<List<Long>> unitIds){
        unitService.deleteUnitsForProperty(id, unitIds.orElse(new ArrayList<>()));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
