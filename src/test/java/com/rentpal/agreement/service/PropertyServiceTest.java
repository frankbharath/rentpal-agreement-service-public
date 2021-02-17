package com.rentpal.agreement.service;

import com.rentpal.agreement.AbstractTest;
import com.rentpal.agreement.common.DTOModelMapper;
import com.rentpal.agreement.common.Utils;
import com.rentpal.agreement.dto.PropertyDTO;
import com.rentpal.agreement.exception.APIRequestException;
import com.rentpal.agreement.model.Property;
import com.rentpal.agreement.model.User;
import com.rentpal.agreement.repository.PropertyRepository;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * @author frank
 * @created 16 Dec,2020 - 3:41 AM
 */

@PrepareForTest({PropertyServiceImpl.class})
public class PropertyServiceTest extends AbstractTest {

    /** The property service. */

    @InjectMocks
    PropertyServiceImpl propertyService;

    @Mock
    PropertyRepository propertyRepository;

    @Mock
    MessageSource messageSource;

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
        Pageable page=mock(Pageable.class);
        String searchQuery="test";
        whenNew(User.class).withNoArguments().thenReturn(user);
        when(propertyRepository.findByUser(user, page)).thenReturn(propertyList);
        when(propertyRepository.findByUserAndTSV(Utils.getUserId(), searchQuery, page)).thenReturn(propertyList);

        assertEquals(2, propertyService.getProperties(null, null, null).size());
        assertEquals(2, propertyService.getProperties(searchQuery, null, null).size());
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
        assertNotNull(propertyService.addProperty(property));
    }

    @Test
    public void testUpdateProperty() throws Exception {
        User user=mock(User.class);
        Property property=mock(Property.class);
        whenNew(User.class).withNoArguments().thenReturn(user);

        //to check if property does not exist
        when(propertyService.propertyExistsForUser(1l)).thenReturn(false);
        Exception exception=assertThrows(APIRequestException.class, ()->{
            String no_property="error.property.not_exists";
            when(Utils.getMessage(messageSource, no_property)).thenReturn(no_property);
            propertyService.updateProperty(property);
        });
        assertEquals("error.property.not_exists", exception.getMessage());

        when(property.getId()).thenReturn(1l);
        when(propertyService.propertyExistsForUser(1l)).thenReturn(true);
        when(propertyRepository.existsByPropertyNameAndIdNotAndUser(property.getPropertyName(), property.getId(), user)).thenReturn(true);
        exception=assertThrows(APIRequestException.class, ()->{
            String no_property="error.property.duplicate_name";
            when(Utils.getMessage(messageSource, no_property)).thenReturn(no_property);
            propertyService.updateProperty(property);
        });
        assertEquals("error.property.duplicate_name", exception.getMessage());

        when(propertyService.propertyExistsForUser(1l)).thenReturn(true);
        when(propertyRepository.existsByPropertyNameAndIdNotAndUser(property.getPropertyName(), property.getId(), user)).thenReturn(false);

        when(propertyRepository.findByIdAndUser(property.getId(), user)).thenReturn(Optional.of(property));
        when(propertyRepository.save(property)).thenReturn(property);
        assertNotNull(propertyService.updateProperty(property));
    }
}
