package com.rentpal.agreement.service.interfaces;

/**
 * @author frank
 * @created 14 Dec,2020 - 11:59 PM
 */

import com.rentpal.agreement.model.User;

public interface AccountService {
    User addUser(User user);

    boolean userExist(String email);

    Long getUserId(String email);

    void addUser(String email);

    User getUser();
}
