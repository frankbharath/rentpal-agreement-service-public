package com.rentpal.agreement.service.interfaces;

/*
 * @author frank
 * @created 23 Dec,2020 - 9:26 PM
 */

import com.rentpal.agreement.dto.TenantDTO;

public interface TenantService {
    TenantDTO addTenant(Long propertyId, Long unitId, TenantDTO tenantDTO);
}
