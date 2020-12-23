package com.rentpal.agreement.service.interfaces;

/*
 * @author frank
 * @created 23 Dec,2020 - 9:00 PM
 */

import com.rentpal.agreement.model.Unit;

import java.util.List;

public interface UnitService {
    /**
     * Adds a unit to the given property and return DTO.
     *
     * @param propertyId the property id
     * @param unit       the unit
     * @return the unit dto
     */
    Unit addUnitToProperty(Long propertyId, Unit unit);

    /**
     * Updates a unit to the given property and return DTO.
     *
     * @param propertyId the property id
     * @param unit       the unit
     * @return the unit dto
     */
    Unit updateUnitToProperty(Long propertyId, Unit unit);

    /**
     * Gets units for given property.
     *
     * @param propertyId the property id
     * @return the units for property
     */
    List<Unit> getUnitsForProperty(Long propertyId);

    /**
     * Gets a unit for given property.
     *
     * @param propertyId the property id
     * @param unitId     the unit id
     * @return the unit for property
     */
    Unit getUnitForProperty(Long propertyId, Long unitId);

    boolean unitExistsForProperty(Long propertyId, Long unitId);

    /**
     * Deletes a unit for property.
     *
     * @param propertyId the property id
     * @param unitId     the unit id
     */
    void deleteUnitForProperty(Long propertyId, Long unitId);
}
