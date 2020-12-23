package com.rentpal.agreement.common;

/*
 * @author frank
 * @created 12 Dec,2020 - 11:25 PM
 */


import com.rentpal.agreement.dto.PropertyDTO;
import com.rentpal.agreement.dto.TenantDTO;
import com.rentpal.agreement.dto.UnitDTO;
import com.rentpal.agreement.model.Property;
import com.rentpal.agreement.model.Tenant;
import com.rentpal.agreement.model.Unit;
import org.springframework.context.MessageSource;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

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

    public static List<PropertyDTO> propertiesModelDTOMapper(List<Property> properties) {
        List<PropertyDTO> propertiesDTO=new ArrayList<>();
        properties.forEach(property -> {
            propertiesDTO.add(DTOModelMapper.propertyModelDTOMapper(property));
        });
        return propertiesDTO;
    }


    /**
     * Converts unit domain model to DTO
     *
     * @param unit the property
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

    public static List<UnitDTO> unitsModelDTOMapper(List<Unit> units){
        List<UnitDTO> unitDTOs=new ArrayList<>();
        units.forEach(unit -> {
            unitDTOs.add(DTOModelMapper.unitModelDTOMapper(unit));
        });
        return unitDTOs;
    }

    public static TenantDTO tenantModelDTOMapper(Tenant tenant, MessageSource messageSource){
        TenantDTO tenantDTO=new TenantDTO();
        tenantDTO.setId(tenant.getId());
        tenantDTO.setDob(tenant.getDob());
        tenantDTO.setEmail(tenant.getEmail());
        tenantDTO.setFirstName(tenant.getFirstName());
        tenantDTO.setLastName(tenant.getLastName());
        tenantDTO.setMovein(Utils.getDate(tenant.getMovein()));
        tenantDTO.setMoveout(Utils.getDate(tenant.getMoveout()));
        tenantDTO.setUnitId(tenant.getUnit().getId());
        tenantDTO.setOccupants(tenant.getOccupants());
        tenantDTO.setNationality(Utils.getMessage(messageSource, tenant.getNationality()));
        return tenantDTO;
    }

    public static Tenant tenantDTOModelMapper(TenantDTO tenantDTO) throws ParseException {
        Tenant tenant=new Tenant();
        tenant.setId(tenantDTO.getId());
        tenant.setMovein(Utils.parseDateToMilliseconds(tenantDTO.getMovein()));
        tenant.setMoveout(Utils.parseDateToMilliseconds(tenantDTO.getMoveout()));
        Unit unit=new Unit();
        unit.setId(tenantDTO.getUnitId());
        tenant.setUnit(unit);
        tenant.setDob(tenantDTO.getDob());
        tenant.setEmail(tenantDTO.getEmail());
        tenant.setFirstName(tenantDTO.getFirstName());
        tenant.setLastName(tenantDTO.getLastName());
        tenant.setNationality(tenantDTO.getNationality());
        tenant.setOccupants(tenantDTO.getOccupants());
        return tenant;
    }
}
