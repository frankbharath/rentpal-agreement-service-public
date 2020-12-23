package com.rentpal.agreement.service;

import com.rentpal.agreement.common.Utils;
import com.rentpal.agreement.exception.APIRequestException;
import com.rentpal.agreement.model.Property;
import com.rentpal.agreement.model.Unit;
import com.rentpal.agreement.repository.UnitRepository;
import com.rentpal.agreement.service.interfaces.PropertyService;
import com.rentpal.agreement.service.interfaces.UnitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author frank
 * @created 23 Dec,2020 - 9:01 PM
 */

@Slf4j
@Service
@Transactional
public class UnitServiceImpl implements UnitService {

    /**
     * Performs CRUD operations on unit object.
     */
    private final UnitRepository unitRepository;

    private final PropertyService propertyService;

    /**
     * For localization, I18N
     */
    private final MessageSource messageSource;

    public UnitServiceImpl(UnitRepository unitRepository, PropertyService propertyService, MessageSource messageSource){
        this.unitRepository=unitRepository;
        this.messageSource=messageSource;
        this.propertyService=propertyService;
    }

    /**
     * Adds a unit to the given property and returns DTO.
     *
     * @param propertyId the property id
     * @param unit       the unit
     * @return the unit dto
     */
    @Override
    public Unit addUnitToProperty(Long propertyId, Unit unit){
        Property property=new Property();
        property.setId(propertyId);
        unit.setProperty(property);
        //check if property exists
        if(!propertyService.propertyExistsForUser(propertyId)){
            throw new APIRequestException(Utils.getMessage(messageSource,"error.property.not_exists"));
        }
        //check if a unit exist for given door number and floor number
        if(unitRepository.existsByDoorNumberAndFloorNumberAndProperty(unit.getDoorNumber(), unit.getFloorNumber(), property)){
            throw new APIRequestException(Utils.getMessage(messageSource,"error.unit.exists"));
        }
        log.info("Adding unit for the property {} and for the user {}", propertyId, Utils.getUserId());
        return unitRepository.save(unit);
    }

    /**
     * Updates a unit to the given property and returns DTO.
     *
     * @param propertyId the property id
     * @param unit       the unit
     * @return the unit dto
     */
    @Override
    public Unit updateUnitToProperty(Long propertyId, Unit unit){
        Property property=new Property();
        property.setId(propertyId);
        //check if property exists
        if(!propertyService.propertyExistsForUser(propertyId)){
            throw new APIRequestException(Utils.getMessage(messageSource,"error.property.not_exists"));
        }

        //check if a unit exist for given door number and floor number
        if(unitRepository.existsByDoorNumberAndFloorNumberAndPropertyAndIdNot(unit.getDoorNumber(), unit.getFloorNumber(), property, unit.getId())){
            throw new APIRequestException(Utils.getMessage(messageSource,"error.unit.exists"));
        }
        unit.setProperty(property);
        log.info("updating unit {} for the property {} and for the user {}", unit.getId(), propertyId, Utils.getUserId());
        return unitRepository.save(unit);
    }

    /**
     * Gets units for given property.
     *
     * @param propertyId the property id
     * @return the units for property
     */
    @Override
    public List<Unit> getUnitsForProperty(Long propertyId){
        Property property=new Property();
        property.setId(propertyId);
        if(!propertyService.propertyExistsForUser(propertyId)){
            throw new APIRequestException(Utils.getMessage(messageSource,"error.property.not_exists"));
        }
        List<Unit> units = unitRepository.getByProperty(property);
        log.info("Units successfully retrieved for property {}", propertyId);
        return units;
    }

    /**
     * Gets a unit for given property.
     *
     * @param propertyId the property id
     * @param unitId     the unit id
     * @return the unit for property
     */
    @Override
    public Unit getUnitForProperty(Long propertyId, Long unitId){
        Property property=new Property();
        property.setId(propertyId);
        if(!propertyService.propertyExistsForUser(propertyId)){
            throw new APIRequestException(Utils.getMessage(messageSource,"error.property.not_exists"));
        }
        log.info("Retrieving the unit {} for the property {}", propertyId, unitId);
        return unitRepository.getByPropertyAndId(property, unitId).orElseThrow(()->{
            throw new APIRequestException(Utils.getMessage(messageSource, "error.unit.not.exists"));
        });
    }

    @Override
    public boolean unitExistsForProperty(Long propertyId, Long unitId){
        Property property=new Property();
        property.setId(propertyId);
        if(!propertyService.propertyExistsForUser(propertyId)){
            throw new APIRequestException(Utils.getMessage(messageSource,"error.property.not_exists"));
        }
        return unitRepository.existsByIdAndProperty(unitId, property);
    }

    /**
     * Deletes a unit for property.
     *
     * @param propertyId the property id
     * @param unitId     the unit id
     */
    @Override
    public void deleteUnitForProperty(Long propertyId, Long unitId){
        Property property=new Property();
        property.setId(propertyId);
        if(!propertyService.propertyExistsForUser(propertyId)){
            throw new APIRequestException(Utils.getMessage(messageSource,"error.property.not_exists"));
        }
        log.info("Deleting the unit {} for the property {}", propertyId, unitId);
        unitRepository.deleteByIdAndProperty(unitId, property);
    }
}
