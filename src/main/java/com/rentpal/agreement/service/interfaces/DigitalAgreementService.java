package com.rentpal.agreement.service.interfaces;

/*
 * @author frank
 * @created 23 Dec,2020 - 9:59 PM
 */

import com.rentpal.agreement.model.Property;
import com.rentpal.agreement.model.Tenant;
import com.rentpal.agreement.model.Unit;
import org.json.JSONObject;

public interface DigitalAgreementService {
    void generateAccessToken();

    JSONObject getRequestDetails(Long requestId);

    String createSignRequest(Tenant tenant, Property property, Unit unit, int retry);
}
