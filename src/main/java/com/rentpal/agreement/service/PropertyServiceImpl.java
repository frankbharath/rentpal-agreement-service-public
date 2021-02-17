package com.rentpal.agreement.service;

import com.rentpal.agreement.common.Constants;
import com.rentpal.agreement.common.DTOModelMapper;
import com.rentpal.agreement.common.Utils;
import com.rentpal.agreement.dto.PropertyDTO;
import com.rentpal.agreement.dto.TenantDTO;
import com.rentpal.agreement.dto.UnitDTO;
import com.rentpal.agreement.exception.APIRequestException;
import com.rentpal.agreement.model.*;
import com.rentpal.agreement.repository.PropertyRepository;
import com.rentpal.agreement.repository.TenantRepository;
import com.rentpal.agreement.repository.UnitRepository;
import com.rentpal.agreement.service.interfaces.EmailService;
import com.rentpal.agreement.service.interfaces.PropertyService;
import com.rentpal.agreement.service.interfaces.UnitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
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
     * For localization, I18N
     */
    private final MessageSource messageSource;


    /**
     * Instantiates a new Property service.
     *
     * @param propertyRepository the property repository
     * @param messageSource      the message source
     */
    public PropertyServiceImpl(PropertyRepository propertyRepository, MessageSource messageSource){
        this.propertyRepository=propertyRepository;
        this.messageSource=messageSource;
    }

    /**
     * Gets property for given id.
     *
     * @param id the id
     * @return the property
     */
    @Override
    public Property getProperty(Long id){
        User user=new User();
        user.setId(Utils.getUserId());
        // gets a property for the current logged in user and exception
        // will be thrown if there is no property
        Property property=propertyRepository.findByIdAndUser(id, user).orElseThrow(()->{
            throw new APIRequestException(Utils.getMessage(messageSource,"error.property.not_exists"));
        });
        log.info("Property {} retrieved successfully", id);
        return property;
    }

    /**
     * Gets all the properties or for given query.
     *
     * @param searchQuery the search query
     * @return the properties
     */
    @Override
    public List<Property> getProperties(String searchQuery, Integer page, Integer size){
        User user=new User();
        user.setId(Utils.getUserId());
        List<Property> properties;
        // retrieves properties from the database based on search condition
        // using FTS with GIN indexing to speed up the query
        Pageable paging = PageRequest.of(page, size==null?Integer.MAX_VALUE:size);
        if(searchQuery!=null && searchQuery.trim().length()>0){
            properties=propertyRepository.findByUserAndTSV(Utils.getUserId(), String.join("|", searchQuery.split("\\s+")), paging);
        }else{
            properties=propertyRepository.findByUser(user, paging);
        }
        log.info("Properties retrieved successfully");
        return properties;
    }

    @Override
    public Long getPropertiesCount(String searchQuery){
        User user=new User();
        user.setId(Utils.getUserId());
        if(searchQuery!=null && searchQuery.trim().length()>0){
            return propertyRepository.countByUserAndTSV(user.getId(), String.join("|", searchQuery.split("\\s+")));
        }
        return propertyRepository.countByUser(user);
    }
    /**
     * Adds property to the database and return DTO.
     *
     * @param property the property
     * @return the property dto
     */
    @Override
    public Property addProperty(Property property){
        User user=new User();
        user.setId(Utils.getUserId());
        //check if property name already exists and throws error exception if exists
        if(propertyRepository.existsByPropertyNameAndUser(property.getPropertyName(), user)){
            throw new APIRequestException(Utils.getMessage(messageSource,"error.property.duplicate_name"));
        }
        property.setUser(user);
        property.setCreationTime(System.currentTimeMillis());
        /*for(int i=0;i<110;i++){
            Property property1=new Property();
            property1.setPropertyName(property.getPropertyName()+"_"+i);
            property1.setCity(property.getCity());
            property1.setAddressLine1(property.getAddressLine1());
            property1.setAddressLine2(property.getAddressLine2());
            property1.setPostal(property.getPostal());
            property1.setUser(user);
            property1.setCreationTime(System.currentTimeMillis());
            propertyRepository.save(property1);
        }*/
        log.info("Adding property for user {}", Utils.getUserId());
        return propertyRepository.save(property);
    }

    /**
     * Updates property to the database and return DTO.
     *
     * @param property the property
     * @return the property dto
     */
    @Override
    public Property updateProperty(Property property){
        User user=new User();
        user.setId(Utils.getUserId());
        //check if property exists
        if(!propertyExistsForUser(property.getId())){
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
        log.info("Updating property for user {}", Utils.getUserId());
        return propertyRepository.save(property);
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

    @Override
    public void deleteProperties(List<Long> propertyIds){
        if(propertyIds.isEmpty()){
            return;
        }
        User user=new User();
        user.setId(Utils.getUserId());
        List<Property> properties=propertyRepository.findByIdInAndUser(propertyIds,user);
        if(!properties.isEmpty()) {
            propertyRepository.deleteAll(properties);
        }
    }

    @Override
    public boolean propertyExistsForUser(Long propertyId){
        User user=new User();
        user.setId(Utils.getUserId());
        return propertyRepository.existsByIdAndUser(propertyId, user);
    }
}
