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

public interface PropertyService {
    PropertyDTO getProperty(Long id);

    List<PropertyDTO> getProperties(String searchQuery);

    PropertyDTO addProperty(Property property);

    PropertyDTO updateProperty(Property property);

    void deleteProperty(Long id);

    UnitDTO addUnitToProperty(Long propertyId, Unit unit);

    UnitDTO updateUnitToProperty(Long propertyId, Unit unit);

    List<UnitDTO> getUnitsForProperty(Long propertyId);

    UnitDTO getUnitForProperty(Long propertyId, Long unitId);

    void deleteUnitForProperty(Long propertyId, Long unitId);
}
