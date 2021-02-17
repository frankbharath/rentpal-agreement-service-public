package com.rentpal.agreement.service;/*
 * @author frank
 * @created 13 Feb,2021 - 5:21 PM
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rentpal.agreement.common.RentpalThreadLocal;
import com.rentpal.agreement.dto.TenantDTO;
import com.rentpal.agreement.model.InitialData;
import com.rentpal.agreement.model.Property;
import com.rentpal.agreement.model.Unit;
import com.rentpal.agreement.service.interfaces.PropertyService;
import com.rentpal.agreement.service.interfaces.TenantService;
import com.rentpal.agreement.service.interfaces.UnitService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InitData {

    private final PropertyService propertyService;

    private final UnitService unitService;

    private TenantService tenantService;

    public InitData(PropertyService propertyService, UnitService unitService) {
        this.propertyService = propertyService;
        this.unitService = unitService;
    }

    @Autowired
    public void setTenantService(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    public void initData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            InitialData initialData=objectMapper.readValue(new ClassPathResource(
                    "init_data.json").getURL(), InitialData.class);
            List<Property> properties=initialData.getProperties();
            for(Property property:properties){
                propertyService.addProperty(property);
            }
            List<Unit> units=initialData.getUnits();
            int i=0;
            for(Unit unit:units){
                unitService.addUnitToProperty(properties.get(i++).getId(), unit);
            }
            List<TenantDTO> tenants=initialData.getTenants();
            i=0;
            for(TenantDTO tenant:tenants){
                i=i%properties.size();
                tenant.setPropertyId(properties.get(i).getId());
                tenant.setUnitId(units.get(i).getId());
                tenantService.addTenant(tenant);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
