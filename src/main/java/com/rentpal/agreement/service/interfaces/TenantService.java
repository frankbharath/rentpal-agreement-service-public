package com.rentpal.agreement.service.interfaces;

/*
 * @author frank
 * @created 23 Dec,2020 - 9:26 PM
 */

import com.rentpal.agreement.dto.TenantDTO;
import com.rentpal.agreement.model.Tenant;
import org.json.JSONObject;

import java.util.List;

public interface TenantService {
    TenantDTO addTenant(TenantDTO tenantDTO);

    void deleteTenant(Long tenantId);

    List<Tenant> getAllTenants(Integer page, Integer size);

    JSONObject getRentHistory();

    Long getAllTenantsCount();
}
