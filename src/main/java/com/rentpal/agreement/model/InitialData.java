package com.rentpal.agreement.model;

import com.rentpal.agreement.dto.TenantDTO;

import java.util.List;

/**
 * @author frank
 * @created 13 Feb,2021 - 7:18 PM
 */

public class InitialData {
    private List<Property> properties;
    private List<Unit> units;
    private List<TenantDTO> tenants;

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }

    public List<TenantDTO> getTenants() {
        return tenants;
    }

    public void setTenants(List<TenantDTO> tenants) {
        this.tenants = tenants;
    }
}
