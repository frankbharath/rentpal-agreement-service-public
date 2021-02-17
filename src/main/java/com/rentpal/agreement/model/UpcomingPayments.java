package com.rentpal.agreement.model;

import java.util.Date;

/**
 * @author frank
 * @created 12 Feb,2021 - 1:08 PM
 */

public interface UpcomingPayments {
    String getFirstName();
    String getLastName();
    String getEmail();
    Date getNextPayment();
}
