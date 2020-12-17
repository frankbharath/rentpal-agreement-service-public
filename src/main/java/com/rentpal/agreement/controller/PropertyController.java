package com.rentpal.agreement.controller;

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

/**
 * @author frank
 * @created 15 Dec,2020 - 12:11 AM
 * Controller that performs CRUD operation on property and its units.
 */

@RestController
@RequestMapping("/properties")
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
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getProperties(@RequestParam Optional<String> searchQuery){
        return new ResponseEntity<>(Utils.getApiRequestResponse(propertyService.getProperties(searchQuery.orElse(null))), HttpStatus.OK);
    }

    /**
     * Gets a single property for a given id.
     *
     * @param id the id
     * @return the response entity
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getProperty(@PathVariable Long id){
        return new ResponseEntity<>(Utils.getApiRequestResponse(propertyService.getProperty(id)), HttpStatus.OK);
    }

    /**
     * Adds property to the database.
     *
     * @param property      the property
     * @param bindingResult the binding result
     * @return the response entity
     * @throws BindException the bind exception
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addProperty(@RequestBody Property property, BindingResult bindingResult) throws BindException {
        propertyValidator.validate(property, bindingResult);
        if(bindingResult.hasErrors()){
            throw new BindException(bindingResult);
        }
        return new ResponseEntity<>(Utils.getApiRequestResponse(propertyService.addProperty(property)), HttpStatus.OK);
    }

    /**
     * Updates property to the database.
     *
     * @param id            the id
     * @param property      the property
     * @param bindingResult the binding result
     * @return the response entity
     * @throws BindException the bind exception
     */
    @PutMapping(value= "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateProperty(@PathVariable Long id, @RequestBody Property property, BindingResult bindingResult) throws BindException {
        propertyValidator.validate(property, bindingResult);
        if(bindingResult.hasErrors()){
            throw new BindException(bindingResult);
        }
        property.setId(id);
        return new ResponseEntity<>(Utils.getApiRequestResponse(propertyService.updateProperty(property)), HttpStatus.OK);
    }

    /**
     * Delete a property from the database for given id.
     *
     * @param id the id
     * @return the response entity
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void>  deleteProperty(@PathVariable Long id){
        propertyService.deleteProperty(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    /**
     * Gets units for given property.
     *
     * @param id the id
     * @return the response entity
     */
    @GetMapping(value = "/{id}/units", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUnitsForProperty(@PathVariable Long id){
        return new ResponseEntity<>(Utils.getApiRequestResponse(propertyService.getUnitsForProperty(id)), HttpStatus.OK);
    }

    /**
     * Gets a unit for given and property.
     *
     * @param id     the id
     * @param unitId the unit id
     * @return the response entity
     */
    @GetMapping(value = "/{id}/units/{unitId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUnitForProperty(@PathVariable Long id, @PathVariable Long unitId){
        return new ResponseEntity<>(Utils.getApiRequestResponse(propertyService.getUnitForProperty(id, unitId)), HttpStatus.OK);
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
    @PostMapping(value = "/{id}/units", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addUnitToProperty(@PathVariable Long id, @RequestBody Unit unit, BindingResult bindingResult) throws BindException {
        unitValidator.validate(unit, bindingResult);
        if(bindingResult.hasErrors()){
            throw new BindException(bindingResult);
        }
        return new ResponseEntity<>(Utils.getApiRequestResponse(propertyService.addUnitToProperty(id, unit)), HttpStatus.OK);
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
    @PutMapping(value = "/{id}/units/{unitId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateUnitToProperty(@PathVariable Long id, @PathVariable Long unitId, @RequestBody Unit unit,  BindingResult bindingResult) throws BindException {
        unit.setId(unitId);
        unitValidator.validate(unit, bindingResult);
        if(bindingResult.hasErrors()){
            throw new BindException(bindingResult);
        }
        return new ResponseEntity<>(Utils.getApiRequestResponse(propertyService.updateUnitToProperty(id, unit)), HttpStatus.OK);
    }

    /**
     * Deletes unit for given id and property.
     *
     * @param id     the id
     * @param unitId the unit id
     * @return the response entity
     */
    @DeleteMapping(value = "/{id}/units/{unitId}")
    public ResponseEntity<Void>  deleteUnitForProperty(@PathVariable Long id, @PathVariable Long unitId){
        propertyService.deleteUnitForProperty(id, unitId);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
