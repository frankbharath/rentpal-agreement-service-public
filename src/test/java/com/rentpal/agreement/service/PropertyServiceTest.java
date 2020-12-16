package com.rentpal.agreement.service;

/*
 * @author frank
 * @created 16 Dec,2020 - 3:41 AM
 */


import com.rentpal.agreement.AbstractTest;
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
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.powermock.api.mockito.PowerMockito.*;

@PrepareForTest({PropertyServiceImpl.class})
public class PropertyServiceTest extends AbstractTest {

    /** The property service. */

    @InjectMocks
    PropertyServiceImpl propertyService;

    @Mock
    PropertyRepository propertyRepository;

    @Mock
    MessageSource messageSource;

    @Mock
    UnitRepository unitRepository;

    @Test(expected = APIRequestException.class)
    public void testGetPropertyIfNotExist() throws Exception {
        User user= mock(User.class);
        whenNew(User.class).withNoArguments().thenReturn(user);
        when(propertyRepository.findByIdAndUser(1l, user)).thenReturn(Optional.empty());
        propertyService.getProperty(1l);
    }

    @Test
    public void testGetProperty() throws Exception {
        User user=mock(User.class);
        Property property=mock(Property.class);
        PropertyDTO propertyDTO=mock(PropertyDTO.class);
        Long propertyId=1l;

        whenNew(User.class).withNoArguments().thenReturn(user);
        when(DTOModelMapper.propertyModelDTOMapper(property)).thenReturn(propertyDTO);
        when(propertyRepository.findByIdAndUser(propertyId, user)).thenReturn(Optional.of(property));

        assertNotNull(propertyService.getProperty(propertyId));
    }

    @Test
    public void testGetProperties() throws Exception {
        User user=mock(User.class);
        Property property=mock(Property.class);
        List<Property> propertyList=new ArrayList<>();
        propertyList.add(property);
        propertyList.add(property);

        String searchQuery="test";
        whenNew(User.class).withNoArguments().thenReturn(user);
        when(propertyRepository.findByUser(user)).thenReturn(propertyList);
        when(propertyRepository.findByUserAndTSV(Utils.getUserId(), searchQuery)).thenReturn(propertyList);

        assertEquals(2, propertyService.getProperties(null).size());
        assertEquals(2, propertyService.getProperties(searchQuery).size());
    }

    @Test
    public void testAddProperty() throws Exception {
        User user=mock(User.class);
        Property property=mock(Property.class);
        PropertyDTO propertyDTO=mock(PropertyDTO.class);
        whenNew(User.class).withNoArguments().thenReturn(user);
        when(property.getPropertyName()).thenReturn("test");
        when(propertyRepository.existsByPropertyNameAndUser(property.getPropertyName(), user)).thenReturn(true);
        assertThrows(APIRequestException.class, ()->{
            propertyService.addProperty(property);
        });
        when(propertyRepository.existsByPropertyNameAndUser(property.getPropertyName(), user)).thenReturn(false);
        when(propertyRepository.save(property)).thenReturn(property);
        when(DTOModelMapper.propertyModelDTOMapper(property)).thenReturn(propertyDTO);
        assertNotNull(propertyService.addProperty(property));
    }

    @Test
    public void testUpdateProperty() throws Exception {
        User user=mock(User.class);
        Property property=mock(Property.class);
        PropertyDTO propertyDTO=mock(PropertyDTO.class);

        whenNew(User.class).withNoArguments().thenReturn(user);

        //to check if property does not exist
        when(propertyService, "propertyExistsForUser", property).thenReturn(false);
        Exception exception=assertThrows(APIRequestException.class, ()->{
            String no_property="error.property.not_exists";
            when(Utils.getMessage(messageSource, no_property)).thenReturn(no_property);
            propertyService.updateProperty(property);
        });
        assertEquals("error.property.not_exists", exception.getMessage());

        when(propertyService, "propertyExistsForUser", property).thenReturn(true);
        when(propertyRepository.existsByPropertyNameAndIdNotAndUser(property.getPropertyName(), property.getId(), user)).thenReturn(true);
        exception=assertThrows(APIRequestException.class, ()->{
            String no_property="error.property.duplicate_name";
            when(Utils.getMessage(messageSource, no_property)).thenReturn(no_property);
            propertyService.updateProperty(property);
        });
        assertEquals("error.property.duplicate_name", exception.getMessage());

        when(propertyService, "propertyExistsForUser", property).thenReturn(true);
        when(propertyRepository.existsByPropertyNameAndIdNotAndUser(property.getPropertyName(), property.getId(), user)).thenReturn(false);

        when(propertyRepository.findByIdAndUser(property.getId(), user)).thenReturn(Optional.of(property));
        when(DTOModelMapper.propertyModelDTOMapper(property)).thenReturn(propertyDTO);
        assertNotNull(propertyService.updateProperty(property));
    }

    @Test
    public void testAddUnitToProperty() throws Exception {
        User user=mock(User.class);
        Property property=mock(Property.class);
        Unit unit=mock(Unit.class);

        whenNew(User.class).withNoArguments().thenReturn(user);
        whenNew(Property.class).withNoArguments().thenReturn(property);

        when(propertyService, "propertyExistsForUser", property).thenReturn(false);
        Exception exception=assertThrows(APIRequestException.class, ()->{
            String no_property="error.property.not_exists";
            when(Utils.getMessage(messageSource, no_property)).thenReturn(no_property);
            propertyService.addUnitToProperty(1l, unit);
        });
        assertEquals("error.property.not_exists", exception.getMessage());

        when(propertyService, "propertyExistsForUser", property).thenReturn(true);
        when(unitRepository.existsByDoorNumberAndFloorNumberAndProperty(unit.getDoorNumber(), unit.getFloorNumber(), property)).thenReturn(true);
        exception=assertThrows(APIRequestException.class, ()->{
            String no_property="error.unit.exists";
            when(Utils.getMessage(messageSource, no_property)).thenReturn(no_property);
            propertyService.addUnitToProperty(property.getId(), unit);
        });
        assertEquals("error.unit.exists", exception.getMessage());

        when(propertyService, "propertyExistsForUser", property).thenReturn(true);
        when(unitRepository.existsByDoorNumberAndFloorNumberAndProperty(unit.getDoorNumber(), unit.getFloorNumber(), property)).thenReturn(false);

        UnitDTO unitDTO=mock(UnitDTO.class);
        when(DTOModelMapper.unitModelDTOMapper(unit)).thenReturn(unitDTO);
        when(unitRepository.save(unit)).thenReturn(unit);
        assertNotNull(propertyService.addUnitToProperty(property.getId(), unit));
    }

    @Test
    public void testUpdateUnitToProperty() throws Exception {
        User user=mock(User.class);
        Property property=mock(Property.class);
        Unit unit=mock(Unit.class);

        whenNew(User.class).withNoArguments().thenReturn(user);
        whenNew(Property.class).withNoArguments().thenReturn(property);

        when(propertyService, "propertyExistsForUser", property).thenReturn(false);
        Exception exception=assertThrows(APIRequestException.class, ()->{
            String no_property="error.property.not_exists";
            when(Utils.getMessage(messageSource, no_property)).thenReturn(no_property);
            propertyService.updateUnitToProperty(1l, unit);
        });
        assertEquals("error.property.not_exists", exception.getMessage());

        when(propertyService, "propertyExistsForUser", property).thenReturn(true);
        when(unitRepository.existsByDoorNumberAndFloorNumberAndPropertyAndIdNot(unit.getDoorNumber(), unit.getFloorNumber(), property, unit.getId())).thenReturn(true);
        exception=assertThrows(APIRequestException.class, ()->{
            String no_property="error.unit.exists";
            when(Utils.getMessage(messageSource, no_property)).thenReturn(no_property);
            propertyService.updateUnitToProperty(property.getId(), unit);
        });
        assertEquals("error.unit.exists", exception.getMessage());

        when(propertyService, "propertyExistsForUser", property).thenReturn(true);
        when(unitRepository.existsByDoorNumberAndFloorNumberAndPropertyAndIdNot(unit.getDoorNumber(), unit.getFloorNumber(), property, unit.getId())).thenReturn(false);

        UnitDTO unitDTO=mock(UnitDTO.class);
        when(DTOModelMapper.unitModelDTOMapper(unit)).thenReturn(unitDTO);
        when(unitRepository.save(unit)).thenReturn(unit);
        assertNotNull(propertyService.updateUnitToProperty(property.getId(), unit));
    }

    @Test
    public void testGetUnitsForProperty() throws Exception {
        User user=mock(User.class);
        Property property=mock(Property.class);

        whenNew(Property.class).withNoArguments().thenReturn(property);
        whenNew(User.class).withNoArguments().thenReturn(user);

        when(propertyService, "propertyExistsForUser", property).thenReturn(false);
        Exception exception=assertThrows(APIRequestException.class, ()->{
            String no_property="error.property.not_exists";
            when(Utils.getMessage(messageSource, no_property)).thenReturn(no_property);
            propertyService.getUnitsForProperty(property.getId());
        });
        assertEquals("error.property.not_exists", exception.getMessage());

        Unit unit=mock(Unit.class);
        List<Unit> units=new ArrayList<>();
        units.add(unit);
        units.add(unit);
        UnitDTO unitDTO=mock(UnitDTO.class);
        when(DTOModelMapper.unitModelDTOMapper(unit)).thenReturn(unitDTO);
        when(unitRepository.getByProperty(property)).thenReturn(units);
        when(propertyService, "propertyExistsForUser", property).thenReturn(true);
        assertEquals(2, propertyService.getUnitsForProperty(1l).size());
    }

    @Test
    public void testGetUnitForProperty() throws Exception {
        User user=mock(User.class);
        Property property=mock(Property.class);
        Unit unit=mock(Unit.class);

        whenNew(Property.class).withNoArguments().thenReturn(property);
        whenNew(User.class).withNoArguments().thenReturn(user);

        when(propertyService, "propertyExistsForUser", property).thenReturn(false);
        Exception exception=assertThrows(APIRequestException.class, ()->{
            String no_property="error.property.not_exists";
            when(Utils.getMessage(messageSource, no_property)).thenReturn(no_property);
            propertyService.getUnitForProperty(property.getId(), unit.getId());
        });
        assertEquals("error.property.not_exists", exception.getMessage());

        when(unitRepository.getByPropertyAndId(property, unit.getId())).thenReturn(Optional.of(unit));
        when(propertyService, "propertyExistsForUser", property).thenReturn(true);
        when(DTOModelMapper.unitModelDTOMapper(unit)).thenReturn(mock(UnitDTO.class));
        assertNotNull(propertyService.getUnitForProperty(property.getId(), unit.getId()));
    }
}
