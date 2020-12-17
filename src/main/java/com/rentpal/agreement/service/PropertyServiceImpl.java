package com.rentpal.agreement.service;

import com.rentpal.agreement.common.DTOModelMapper;
import com.rentpal.agreement.common.Utils;
import com.rentpal.agreement.dto.PropertyDTO;
import com.rentpal.agreement.dto.UnitDTO;
import com.rentpal.agreement.exception.APIRequestException;
import com.rentpal.agreement.model.Property;
import com.rentpal.agreement.model.Unit;
import com.rentpal.agreement.model.User;
import com.rentpal.agreement.repository.PropertyRepository;
import com.rentpal.agreement.repository.UnitRepository;
import com.rentpal.agreement.service.interfaces.PropertyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Property service.
 *
 * @author frank
 * @created 15 Dec,2020 - 12:45 AM Service class that performs business logic on property and unit object.
 */

@Service
@Transactional
@Slf4j
public class PropertyServiceImpl implements PropertyService {

    /**
     * Performs CRUD operations on property object.
     */
    private final PropertyRepository propertyRepository;

    /**
     * Performs CRUD operations on unit object.
     */
    private final UnitRepository unitRepository;

    /**
     * For localization, I18N
     */
    private final MessageSource messageSource;

    /**
     * Instantiates a new Property service.
     *
     * @param propertyRepository the property repository
     * @param unitRepository     the unit repository
     * @param messageSource      the message source
     */
    public PropertyServiceImpl(PropertyRepository propertyRepository, UnitRepository unitRepository, MessageSource messageSource){
        this.propertyRepository=propertyRepository;
        this.unitRepository=unitRepository;
        this.messageSource=messageSource;
    }

    /**
     * Gets property for given id.
     *
     * @param id the id
     * @return the property
     */
    @Override
    public PropertyDTO getProperty(Long id){
        User user=new User();
        user.setId(Utils.getUserId());
        Property property=propertyRepository.findByIdAndUser(id, user).orElseThrow(()->{
            throw new APIRequestException(Utils.getMessage(messageSource,"error.property.not_exists"));
        });
        log.info("Property {} retrieved successfully", id);
        return DTOModelMapper.propertyModelDTOMapper(property);
    }

    /**
     * Gets all the properties or for given query.
     *
     * @param searchQuery the search query
     * @return the properties
     */
    @Override
    public List<PropertyDTO> getProperties(String searchQuery){
        User user=new User();
        user.setId(Utils.getUserId());
        List<Property> properties;
        if(searchQuery!=null){
            properties=propertyRepository.findByUserAndTSV(Utils.getUserId(), String.join("|", searchQuery.split("\\s+")));
        }else{
            properties=propertyRepository.findByUser(user);
        }
        List<PropertyDTO> propertiesDTO=new ArrayList<>();
        properties.forEach(property -> {
            propertiesDTO.add(DTOModelMapper.propertyModelDTOMapper(property));
        });
        log.info("Properties retrieved successfully");
        return propertiesDTO;
    }

    /**
     * Adds property to the database and return DTO.
     *
     * @param property the property
     * @return the property dto
     */
    @Override
    public PropertyDTO addProperty(Property property){
        User user=new User();
        user.setId(Utils.getUserId());
        //check if property name already exists
        if(propertyRepository.existsByPropertyNameAndUser(property.getPropertyName(), user)){
            throw new APIRequestException(Utils.getMessage(messageSource,"error.property.duplicate_name"));
        }
        property.setUser(user);
        property.setCreationTime(System.currentTimeMillis());
        log.info("Property added for user {}", Utils.getUserId());
        return DTOModelMapper.propertyModelDTOMapper(propertyRepository.save(property));
    }

    /**
     * Updates property to the database and return DTO.
     *
     * @param property the property
     * @return the property dto
     */
    @Override
    public PropertyDTO updateProperty(Property property){
        User user=new User();
        user.setId(Utils.getUserId());
        //check if property exists
        if(!propertyExistsForUser(property)){
            throw new APIRequestException(Utils.getMessage(messageSource,"error.property.not_exists"));
        }
        //check if property name already exists
        if(propertyRepository.existsByPropertyNameAndIdNotAndUser(property.getPropertyName(), property.getId(), user)){
            throw new APIRequestException(Utils.getMessage(messageSource,"error.property.duplicate_name"));
        }
        Property dbProperty=propertyRepository.findByIdAndUser(property.getId(), user).orElseThrow(()->{
            throw new APIRequestException(Utils.getMessage(messageSource,"error.property.not_exists"));
        });
        property.setCreationTime(dbProperty.getCreationTime());
        property.setUser(user);
        propertyRepository.save(property);
        log.info("Property updated for user {}", Utils.getUserId());
        return DTOModelMapper.propertyModelDTOMapper(property);
    }

    /**
     * Deletes property from the database for given id.
     *
     * @param id the id
     */
    @Override
    public void deleteProperty(Long id){
        User user=new User();
        user.setId(Utils.getUserId());
        propertyRepository.deleteByIdAndUser(id, user);
        log.info("Deleted property {} for user {}", id, Utils.getUserId());
    }

    /**
     * Adds a unit to the given property and returns DTO.
     *
     * @param propertyId the property id
     * @param unit       the unit
     * @return the unit dto
     */
    @Override
    public UnitDTO addUnitToProperty(Long propertyId, Unit unit){
        Property property=new Property();
        property.setId(propertyId);
        unit.setProperty(property);
        //check if property exists
        if(!propertyExistsForUser(property)){
            throw new APIRequestException(Utils.getMessage(messageSource,"error.property.not_exists"));
        }
        //check if a unit exist for given door number and floor number
        if(unitRepository.existsByDoorNumberAndFloorNumberAndProperty(unit.getDoorNumber(), unit.getFloorNumber(), property)){
            throw new APIRequestException(Utils.getMessage(messageSource,"error.unit.exists"));
        }
        log.info("Added unit for the property {} and for the user {}", propertyId, Utils.getUserId());
        return DTOModelMapper.unitModelDTOMapper(unitRepository.save(unit));
    }

    /**
     * Updates a unit to the given property and returns DTO.
     *
     * @param propertyId the property id
     * @param unit       the unit
     * @return the unit dto
     */
    @Override
    public UnitDTO updateUnitToProperty(Long propertyId, Unit unit){
        Property property=new Property();
        property.setId(propertyId);
        //check if property exists
        if(!propertyExistsForUser(property)){
            throw new APIRequestException(Utils.getMessage(messageSource,"error.property.not_exists"));
        }
        //check if a unit exist for given door number and floor number
        if(unitRepository.existsByDoorNumberAndFloorNumberAndPropertyAndIdNot(unit.getDoorNumber(), unit.getFloorNumber(), property, unit.getId())){
            throw new APIRequestException(Utils.getMessage(messageSource,"error.unit.exists"));
        }
        unit.setProperty(property);
        log.info("updated unit {} for the property {} and for the user {}", unit.getId(), propertyId, Utils.getUserId());
        return DTOModelMapper.unitModelDTOMapper(unitRepository.save(unit));
    }

    /**
     * Gets units for given property.
     *
     * @param propertyId the property id
     * @return the units for property
     */
    @Override
    public List<UnitDTO> getUnitsForProperty(Long propertyId){
        Property property=new Property();
        property.setId(propertyId);
        if(!propertyExistsForUser(property)){
            throw new APIRequestException(Utils.getMessage(messageSource,"error.property.not_exists"));
        }
        List<Unit> units = unitRepository.getByProperty(property);
        List<UnitDTO> unitDTOs=new ArrayList<>();
        units.forEach(unit -> {
            unitDTOs.add(DTOModelMapper.unitModelDTOMapper(unit));
        });
        return unitDTOs;
    }

    /**
     * Gets a unit for given property.
     *
     * @param propertyId the property id
     * @param unitId     the unit id
     * @return the unit for property
     */
    @Override
    public UnitDTO getUnitForProperty(Long propertyId, Long unitId){
        Property property=new Property();
        property.setId(propertyId);
        if(!propertyExistsForUser(property)){
            throw new APIRequestException(Utils.getMessage(messageSource,"error.property.not_exists"));
        }
        return DTOModelMapper.unitModelDTOMapper(unitRepository.getByPropertyAndId(property, unitId).orElseThrow(()->{
            throw new APIRequestException(Utils.getMessage(messageSource, "error.unit.not.exists"));
        }));
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
        if(!propertyExistsForUser(property)){
            throw new APIRequestException(Utils.getMessage(messageSource,"error.property.not_exists"));
        }
        unitRepository.deleteByIdAndProperty(unitId, property);
    }

    private boolean propertyExistsForUser(Property property){
        User user=new User();
        user.setId(Utils.getUserId());
        return propertyRepository.existsByIdAndUser(property.getId(), user);
    }
}
