package com.rentpal.agreement.service;

/*
 * @author frank
 * @created 15 Dec,2020 - 12:45 AM
 */

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
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;

    private final UnitRepository unitRepository;

    private final MessageSource messageSource;

    public PropertyServiceImpl(PropertyRepository propertyRepository, UnitRepository unitRepository, MessageSource messageSource){
        this.propertyRepository=propertyRepository;
        this.unitRepository=unitRepository;
        this.messageSource=messageSource;
    }

    @Override
    public PropertyDTO getProperty(Long id){
        User user=new User();
        user.setId(Utils.getUserId());
        Property property=propertyRepository.findByIdAndUser(id, user).orElseThrow(()->{
            throw new APIRequestException(Utils.getMessage(messageSource,"error.property.not_exists"));
        });
        return DTOModelMapper.propertyModelDTOMapper(property);
    }

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
        return propertiesDTO;
    }

    @Override
    public PropertyDTO addProperty(Property property){
        User user=new User();
        user.setId(Utils.getUserId());
        if(propertyRepository.existsByPropertyNameAndUser(property.getPropertyName(), user)){
            throw new APIRequestException(Utils.getMessage(messageSource,"error.property.duplicate_name"));
        }
        property.setUser(user);
        property.setCreationTime(System.currentTimeMillis());
        return DTOModelMapper.propertyModelDTOMapper(propertyRepository.save(property));
    }

    @Override
    public PropertyDTO updateProperty(Property property){
        User user=new User();
        user.setId(Utils.getUserId());
        if(!propertyExistsForUser(property)){
            throw new APIRequestException(Utils.getMessage(messageSource,"error.property.not_exists"));
        }
        if(propertyRepository.existsByPropertyNameAndIdNotAndUser(property.getPropertyName(), property.getId(), user)){
            throw new APIRequestException(Utils.getMessage(messageSource,"error.property.duplicate_name"));
        }
        Property dbProperty=propertyRepository.findByIdAndUser(property.getId(), user).orElseThrow(()->{
            throw new APIRequestException(Utils.getMessage(messageSource,"error.property.not_exists"));
        });
        property.setCreationTime(dbProperty.getCreationTime());
        property.setUser(user);
        propertyRepository.save(property);
        return DTOModelMapper.propertyModelDTOMapper(property);
    }

    @Override
    public void deleteProperty(Long id){
        User user=new User();
        user.setId(Utils.getUserId());
        propertyRepository.deleteByIdAndUser(id, user);
    }

    @Override
    public UnitDTO addUnitToProperty(Long propertyId, Unit unit){
        Property property=new Property();
        property.setId(propertyId);
        unit.setProperty(property);
        if(!propertyExistsForUser(property)){
            throw new APIRequestException(Utils.getMessage(messageSource,"error.property.not_exists"));
        }
        if(unitRepository.existsByDoorNumberAndFloorNumberAndProperty(unit.getDoorNumber(), unit.getFloorNumber(), property)){
            throw new APIRequestException(Utils.getMessage(messageSource,"error.unit.exists"));
        }
        return DTOModelMapper.unitModelDTOMapper(unitRepository.save(unit));
    }

    @Override
    public UnitDTO updateUnitToProperty(Long propertyId, Unit unit){
        Property property=new Property();
        property.setId(propertyId);
        if(!propertyExistsForUser(property)){
            throw new APIRequestException(Utils.getMessage(messageSource,"error.property.not_exists"));
        }
        if(unitRepository.existsByDoorNumberAndFloorNumberAndPropertyAndIdNot(unit.getDoorNumber(), unit.getFloorNumber(), property, unit.getId())){
            throw new APIRequestException(Utils.getMessage(messageSource,"error.unit.exists"));
        }
        unit.setProperty(property);
        return DTOModelMapper.unitModelDTOMapper(unitRepository.save(unit));
    }

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
