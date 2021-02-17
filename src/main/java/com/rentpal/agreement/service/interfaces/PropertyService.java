package com.rentpal.agreement.service.interfaces;

/*
 * @author frank
 * @created 15 Dec,2020 - 1:49 AM
 */

import com.rentpal.agreement.dto.TenantDTO;
import com.rentpal.agreement.model.Property;

import java.util.List;

/**
 * The interface Property service.
 */
public interface PropertyService {
    /**
     * Gets property for given id.
     *
     * @param id the id
     * @return the property
     */
    Property getProperty(Long id);

    /**
     * Gets all the properties or for given query.
     *
     * @param searchQuery the search query
     * @return the properties
     */
    List<Property> getProperties(String searchQuery, Integer page, Integer size);

    Long getPropertiesCount(String searchQuery);

    /**
     * Adds property to the database and return DTO.
     *
     * @param property the property
     * @return the property dto
     */
    Property addProperty(Property property);

    /**
     * Updates property to the database and return DTO.
     *
     * @param property the property
     * @return the property dto
     */
    Property updateProperty(Property property);

    /**
     * Deletes property from the database for given id.
     *
     * @param id the id
     */
    void deleteProperty(Long id);

    void deleteProperties(List<Long> propertyIds);

    boolean propertyExistsForUser(Long propertyId);
}
