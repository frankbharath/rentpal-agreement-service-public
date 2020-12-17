package com.rentpal.agreement.common;

/*
 * @author frank
 * @created 12 Dec,2020 - 11:25 PM
 */


import com.rentpal.agreement.dto.PropertyDTO;
import com.rentpal.agreement.dto.UnitDTO;
import com.rentpal.agreement.model.Property;
import com.rentpal.agreement.model.Unit;

public class DTOModelMapper {

    /**
     * Converts property domain model to DTO
     *
     * @param property the property
     * @return the property DTO
     */
    public static PropertyDTO propertyModelDTOMapper(Property property) {
        PropertyDTO propertyDTO=new PropertyDTO();
		propertyDTO.setId(property.getId());
        propertyDTO.setUserId(property.getUser().getId());
		propertyDTO.setPropertyname(property.getPropertyName());
        propertyDTO.setAddressline_1(property.getAddressLine1());
		propertyDTO.setAddressline_2(property.getAddressLine2());
		propertyDTO.setPostal(property.getPostal());
		propertyDTO.setCity(property.getCity());
		propertyDTO.setCreationtime(Utils.getDate(property.getCreationTime()));
        return propertyDTO;
    }

    /**
     * Converts unit domain model to DTO
     *
     * @param property the property
     * @return the property DTO
     */
    public static UnitDTO unitModelDTOMapper(Unit unit){
        UnitDTO unitDTO=new UnitDTO();
        unitDTO.setId(unit.getId());
        unitDTO.setPropertyId(unit.getProperty().getId());
        unitDTO.setArea(unit.getArea());
        unitDTO.setBathrooms(unit.getBathrooms());
        unitDTO.setBedrooms(unit.getBedrooms());
        unitDTO.setCautionDeposit(unit.getCautionDeposit());
        unitDTO.setDoorNumber(unit.getDoorNumber());
        unitDTO.setFloorNumber(unit.getFloorNumber());
        unitDTO.setRent(unit.getRent());
        unitDTO.setFurnished(unit.getFurnished());
        return unitDTO;
    }
}
