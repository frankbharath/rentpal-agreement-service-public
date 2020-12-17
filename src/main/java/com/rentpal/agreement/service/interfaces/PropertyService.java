package com.rentpal.agreement.service.interfaces;

/*
 * @author frank
 * @created 15 Dec,2020 - 1:49 AM
 */

import com.rentpal.agreement.dto.PropertyDTO;
import com.rentpal.agreement.dto.UnitDTO;
import com.rentpal.agreement.model.Property;
import com.rentpal.agreement.model.Unit;

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
    PropertyDTO getProperty(Long id);

    /**
     * Gets all the properties or for given query.
     *
     * @param searchQuery the search query
     * @return the properties
     */
    List<PropertyDTO> getProperties(String searchQuery);

    /**
     * Adds property to the database and return DTO.
     *
     * @param property the property
     * @return the property dto
     */
    PropertyDTO addProperty(Property property);

    /**
     * Updates property to the database and return DTO.
     *
     * @param property the property
     * @return the property dto
     */
    PropertyDTO updateProperty(Property property);

    /**
     * Deletes property from the database for given id.
     *
     * @param id the id
     */
    void deleteProperty(Long id);

    /**
     * Adds a unit to the given property and return DTO.
     *
     * @param propertyId the property id
     * @param unit       the unit
     * @return the unit dto
     */
    UnitDTO addUnitToProperty(Long propertyId, Unit unit);

    /**
     * Updates a unit to the given property and return DTO.
     *
     * @param propertyId the property id
     * @param unit       the unit
     * @return the unit dto
     */
    UnitDTO updateUnitToProperty(Long propertyId, Unit unit);

    /**
     * Gets units for given property.
     *
     * @param propertyId the property id
     * @return the units for property
     */
    List<UnitDTO> getUnitsForProperty(Long propertyId);

    /**
     * Gets a unit for given property.
     *
     * @param propertyId the property id
     * @param unitId     the unit id
     * @return the unit for property
     */
    UnitDTO getUnitForProperty(Long propertyId, Long unitId);

    /**
     * Deletes a unit for property.
     *
     * @param propertyId the property id
     * @param unitId     the unit id
     */
    void deleteUnitForProperty(Long propertyId, Long unitId);
}
