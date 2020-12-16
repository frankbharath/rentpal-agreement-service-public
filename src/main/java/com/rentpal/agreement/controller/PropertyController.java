package com.rentpal.agreement.controller;

/*
 * @author frank
 * @created 15 Dec,2020 - 12:11 AM
 */

import com.rentpal.agreement.common.Utils;
import com.rentpal.agreement.model.Property;
import com.rentpal.agreement.model.Unit;
import com.rentpal.agreement.service.interfaces.PropertyService;
import com.rentpal.agreement.validator.PropertyValidator;
import com.rentpal.agreement.validator.UnitValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/properties")
public class PropertyController {

    private final PropertyService propertyService;

    private final PropertyValidator propertyValidator;

    private final UnitValidator unitValidator;

    public PropertyController(PropertyService propertyService, PropertyValidator propertyValidator, UnitValidator unitValidator){
        this.propertyService=propertyService;
        this.propertyValidator=propertyValidator;
        this.unitValidator=unitValidator;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getProperties(@RequestParam Optional<String> searchQuery){
        return new ResponseEntity<>(Utils.getApiRequestResponse(propertyService.getProperties(searchQuery.orElse(null))), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getProperty(@PathVariable Long id){
        return new ResponseEntity<>(Utils.getApiRequestResponse(propertyService.getProperty(id)), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addProperty(@RequestBody Property property, BindingResult bindingResult) throws BindException {
        propertyValidator.validate(property, bindingResult);
        if(bindingResult.hasErrors()){
            throw new BindException(bindingResult);
        }
        return new ResponseEntity<>(Utils.getApiRequestResponse(propertyService.addProperty(property)), HttpStatus.OK);
    }

    @PutMapping(value= "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateProperty(@PathVariable Long id, @RequestBody Property property, BindingResult bindingResult) throws BindException {
        propertyValidator.validate(property, bindingResult);
        if(bindingResult.hasErrors()){
            throw new BindException(bindingResult);
        }
        property.setId(id);
        return new ResponseEntity<>(Utils.getApiRequestResponse(propertyService.updateProperty(property)), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void>  deleteProperty(@PathVariable Long id){
        propertyService.deleteProperty(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/{id}/units", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUnitsForProperty(@PathVariable Long id){
        return new ResponseEntity<>(Utils.getApiRequestResponse(propertyService.getUnitsForProperty(id)), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/units/{unitId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUnitForProperty(@PathVariable Long id, @PathVariable Long unitId){
        return new ResponseEntity<>(Utils.getApiRequestResponse(propertyService.getUnitForProperty(id, unitId)), HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/units", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addUnitToProperty(@PathVariable Long id, @RequestBody Unit unit, BindingResult bindingResult) throws BindException {
        unitValidator.validate(unit, bindingResult);
        if(bindingResult.hasErrors()){
            throw new BindException(bindingResult);
        }
        return new ResponseEntity<>(Utils.getApiRequestResponse(propertyService.addUnitToProperty(id, unit)), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/units/{unitId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateUnitToProperty(@PathVariable Long id, @PathVariable Long unitId, @RequestBody Unit unit){
        unit.setId(unitId);
        return new ResponseEntity<>(Utils.getApiRequestResponse(propertyService.updateUnitToProperty(id, unit)), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}/units/{unitId}")
    public ResponseEntity<Void>  deleteUnitForProperty(@PathVariable Long id, @PathVariable Long unitId){
        propertyService.deleteUnitForProperty(id, unitId);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
