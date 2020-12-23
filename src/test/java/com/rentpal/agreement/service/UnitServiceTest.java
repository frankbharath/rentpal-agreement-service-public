package com.rentpal.agreement.service;

import com.rentpal.agreement.AbstractTest;
import com.rentpal.agreement.common.Utils;
import com.rentpal.agreement.exception.APIRequestException;
import com.rentpal.agreement.model.Property;
import com.rentpal.agreement.model.Unit;
import com.rentpal.agreement.model.User;
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

/*
 * @author frank
 * @created 23 Dec,2020 - 11:46 PM
 */

@PrepareForTest({UnitServiceImpl.class})
public class UnitServiceTest  extends AbstractTest {

    @InjectMocks
    UnitServiceImpl unitService;

    @Mock
    UnitRepository unitRepository;

    @Mock
    MessageSource messageSource;

    @Mock
    PropertyServiceImpl propertyService;

    @Test
    public void testAddUnitToProperty() throws Exception {
        User user=mock(User.class);
        Property property=mock(Property.class);
        Unit unit=mock(Unit.class);

        whenNew(User.class).withNoArguments().thenReturn(user);
        whenNew(Property.class).withNoArguments().thenReturn(property);

        when(property.getId()).thenReturn(1l);
        when(propertyService.propertyExistsForUser(1l)).thenReturn(false);
        Exception exception=assertThrows(APIRequestException.class, ()->{
            String no_property="error.property.not_exists";
            when(Utils.getMessage(messageSource, no_property)).thenReturn(no_property);
            unitService.addUnitToProperty(1l, unit);
        });
        assertEquals("error.property.not_exists", exception.getMessage());

        when(propertyService.propertyExistsForUser(1l)).thenReturn(true);
        when(unitRepository.existsByDoorNumberAndFloorNumberAndProperty(unit.getDoorNumber(), unit.getFloorNumber(), property)).thenReturn(true);
        exception=assertThrows(APIRequestException.class, ()->{
            String no_property="error.unit.exists";
            when(Utils.getMessage(messageSource, no_property)).thenReturn(no_property);
            unitService.addUnitToProperty(property.getId(), unit);
        });
        assertEquals("error.unit.exists", exception.getMessage());

        when(propertyService.propertyExistsForUser(1l)).thenReturn(true);
        when(unitRepository.existsByDoorNumberAndFloorNumberAndProperty(unit.getDoorNumber(), unit.getFloorNumber(), property)).thenReturn(false);

        when(unitRepository.save(unit)).thenReturn(unit);
        assertNotNull(unitService.addUnitToProperty(property.getId(), unit));
    }

    @Test
    public void testUpdateUnitToProperty() throws Exception {
        User user=mock(User.class);
        Property property=mock(Property.class);
        Unit unit=mock(Unit.class);

        whenNew(User.class).withNoArguments().thenReturn(user);
        whenNew(Property.class).withNoArguments().thenReturn(property);

        when(property.getId()).thenReturn(1l);
        when(propertyService.propertyExistsForUser(1l)).thenReturn(false);
        Exception exception=assertThrows(APIRequestException.class, ()->{
            String no_property="error.property.not_exists";
            when(Utils.getMessage(messageSource, no_property)).thenReturn(no_property);
            unitService.updateUnitToProperty(1l, unit);
        });
        assertEquals("error.property.not_exists", exception.getMessage());

        when(propertyService.propertyExistsForUser(1l)).thenReturn(true);
        when(unitRepository.existsByDoorNumberAndFloorNumberAndPropertyAndIdNot(unit.getDoorNumber(), unit.getFloorNumber(), property, unit.getId())).thenReturn(true);
        exception=assertThrows(APIRequestException.class, ()->{
            String no_property="error.unit.exists";
            when(Utils.getMessage(messageSource, no_property)).thenReturn(no_property);
            unitService.updateUnitToProperty(1l, unit);
        });
        assertEquals("error.unit.exists", exception.getMessage());

        when(propertyService.propertyExistsForUser(1l)).thenReturn(true);
        when(unitRepository.existsByDoorNumberAndFloorNumberAndPropertyAndIdNot(unit.getDoorNumber(), unit.getFloorNumber(), property, unit.getId())).thenReturn(false);

        when(unitRepository.save(unit)).thenReturn(unit);
        assertNotNull(unitService.updateUnitToProperty(1l, unit));
    }

    @Test
    public void testGetUnitsForProperty() throws Exception {
        User user=mock(User.class);
        Property property=mock(Property.class);

        whenNew(Property.class).withNoArguments().thenReturn(property);
        whenNew(User.class).withNoArguments().thenReturn(user);

        when(property.getId()).thenReturn(1l);
        when(propertyService.propertyExistsForUser(1l)).thenReturn(false);
        Exception exception=assertThrows(APIRequestException.class, ()->{
            String no_property="error.property.not_exists";
            when(Utils.getMessage(messageSource, no_property)).thenReturn(no_property);
            unitService.getUnitsForProperty(1l);
        });
        assertEquals("error.property.not_exists", exception.getMessage());

        Unit unit=mock(Unit.class);
        List<Unit> units=new ArrayList<>();
        units.add(unit);
        units.add(unit);
        when(unitRepository.getByProperty(property)).thenReturn(units);
        when(propertyService.propertyExistsForUser(1l)).thenReturn(true);
        assertEquals(2, unitService.getUnitsForProperty(1l).size());
    }

    @Test
    public void testGetUnitForProperty() throws Exception {
        User user=mock(User.class);
        Property property=mock(Property.class);
        Unit unit=mock(Unit.class);

        whenNew(Property.class).withNoArguments().thenReturn(property);
        whenNew(User.class).withNoArguments().thenReturn(user);

        when(property.getId()).thenReturn(1l);
        when(propertyService.propertyExistsForUser(1l)).thenReturn(false);
        Exception exception=assertThrows(APIRequestException.class, ()->{
            String no_property="error.property.not_exists";
            when(Utils.getMessage(messageSource, no_property)).thenReturn(no_property);
            unitService.getUnitForProperty(1l, unit.getId());
        });
        assertEquals("error.property.not_exists", exception.getMessage());

        when(unitRepository.getByPropertyAndId(property, unit.getId())).thenReturn(Optional.of(unit));
        when(propertyService.propertyExistsForUser(1l)).thenReturn(true);
        assertNotNull(unitService.getUnitForProperty(property.getId(), unit.getId()));
    }
}
