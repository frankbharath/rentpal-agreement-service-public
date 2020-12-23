package com.rentpal.agreement.controller;

import com.rentpal.agreement.model.Property;
import com.rentpal.agreement.model.Unit;
import com.rentpal.agreement.service.interfaces.UnitService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * @author frank
 * @created 23 Dec,2020 - 11:21 PM
 */

public class UnitControllerTest extends AbstractControllerTest{

    @Autowired
    UnitController unitController;

    @Autowired
    UnitValidator unitValidator;

    @MockBean
    UnitService unitService;

    @Test
    public void testGetUnitsForProperty() throws Exception {
        MvcResult result=mvc.perform(MockMvcRequestBuilders.get("/properties/1/units")).andReturn();
        Assertions.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    @Test
    public void testGetUnitForProperty() throws Exception {
        Unit unit=new Unit();
        Property property=new Property();
        unit.setProperty(property);
        when(unitService.getUnitForProperty(anyLong(), anyLong())).thenReturn(unit);
        MvcResult result=mvc.perform(MockMvcRequestBuilders.get("/properties/1/units/2")).andReturn();
        Assertions.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    @Test
    public void testAddUnitToProperty() throws Exception {
        Unit unit=new Unit();
        MvcResult result=mvc.perform(MockMvcRequestBuilders.post("/properties/1/units")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(unit)))
                .andReturn();
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), result.getResponse().getStatus());

        unit.setArea(111.11f);
        unit.setBathrooms(11111);
        unit.setBedrooms(111111);
        unit.setCautionDeposit(4343.34f);
        unit.setRent(34343.34f);
        unit.setFurnished(false);
        unit.setDoorNumber("34");
        unit.setFloorNumber(1111);
        Property property=new Property();
        unit.setProperty(property);
        when(unitService.addUnitToProperty(anyLong(), any(Unit.class))).thenReturn(unit);
        result=mvc.perform(MockMvcRequestBuilders.post("/properties/1/units")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(unit)))
                .andReturn();
        Assertions.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    @Test
    public void testUpdateUnitToProperty() throws Exception {
        Unit unit=new Unit();
        MvcResult result=mvc.perform(MockMvcRequestBuilders.put("/properties/1/units/2")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(unit)))
                .andReturn();
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), result.getResponse().getStatus());

        unit.setArea(111.11f);
        unit.setBathrooms(11111);
        unit.setBedrooms(111111);
        unit.setCautionDeposit(4343.34f);
        unit.setRent(34343.34f);
        unit.setFurnished(false);
        unit.setDoorNumber("34");
        unit.setFloorNumber(1111);
        Property property=new Property();
        unit.setProperty(property);
        when(unitService.updateUnitToProperty(anyLong(), any(Unit.class))).thenReturn(unit);
        result=mvc.perform(MockMvcRequestBuilders.put("/properties/1/units/2")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(unit)))
                .andReturn();
        Assertions.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    @Test
    public void testDeleteUnitForProperty() throws Exception {
        MvcResult result=mvc.perform(MockMvcRequestBuilders.delete("/properties/1/units/2"))
                .andReturn();
        Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus());
    }
}
