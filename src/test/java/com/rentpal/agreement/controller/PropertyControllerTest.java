package com.rentpal.agreement.controller;

/*
 * @author frank
 * @created 16 Dec,2020 - 11:01 PM
 */

import com.rentpal.agreement.model.*;
import com.rentpal.agreement.service.interfaces.PropertyService;
import com.rentpal.agreement.validator.PropertyValidator;
import com.rentpal.agreement.validator.UnitValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.*;

public class PropertyControllerTest extends AbstractControllerTest {

    @Autowired
    PropertyController propertyController;

    @MockBean
    PropertyService propertyService;

    @Autowired
    PropertyValidator propertyValidator;

    @Test
    public void testGetProperties() throws Exception {
        MvcResult result=mvc.perform(MockMvcRequestBuilders.get("/properties")).andReturn();
        Assertions.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    @Test
    public void testGetProperty() throws Exception {
        Property property=new Property();
        User user=new User();
        property.setUser(user);
        property.setCreationTime(System.currentTimeMillis());
        when(propertyService.getProperty(1l)).thenReturn(property);
        MvcResult result=mvc.perform(MockMvcRequestBuilders.get("/properties/1")).andReturn();
        Assertions.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    @Test
    public void testAddProperty() throws Exception {
        Property property=new Property();
        User user=new User();
        property.setUser(user);
        MvcResult result=mvc.perform(MockMvcRequestBuilders.post("/properties")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(property)))
                .andReturn();
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), result.getResponse().getStatus());

        property.setPropertyName("~test");
        property.setAddressLine1("~test");
        property.setAddressLine2("~test");
        property.setCity("~test");
        property.setPostal("~test");
        result=mvc.perform(MockMvcRequestBuilders.post("/properties")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(property)))
                .andReturn();
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), result.getResponse().getStatus());

        property.setPropertyName("test");
        property.setAddressLine1("10B");
        property.setAddressLine2("Place Des");
        property.setCity("Ivry");
        property.setPostal("94400");
        property.setCreationTime(System.currentTimeMillis());
        when(propertyService.addProperty(any(Property.class))).thenReturn(property);
        result=mvc.perform(MockMvcRequestBuilders.post("/properties")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(property)))
                .andReturn();
        Assertions.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    @Test
    public void testUpdateProperty() throws Exception {
        Property property=new Property();
        User user=new User();
        property.setUser(user);
        String uri="/properties/1";
        MvcResult result=mvc.perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(property)))
                .andReturn();
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), result.getResponse().getStatus());

        property.setPropertyName("~test");
        property.setAddressLine1("~test");
        property.setAddressLine2("~test");
        property.setCity("~test");
        property.setPostal("~test");
        result=mvc.perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(property)))
                .andReturn();
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), result.getResponse().getStatus());

        property.setPropertyName("test");
        property.setAddressLine1("10B");
        property.setAddressLine2("Place Des");
        property.setCity("Ivry");
        property.setPostal("94400");
        property.setCreationTime(System.currentTimeMillis());
        when(propertyService.updateProperty(any(Property.class))).thenReturn(property);
        result=mvc.perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(property)))
                .andReturn();
        Assertions.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    @Test
    public void testDeleteProperty() throws Exception {
        MvcResult result=mvc.perform(MockMvcRequestBuilders.delete("/properties/1"))
                .andReturn();
        Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus());
    }
}
