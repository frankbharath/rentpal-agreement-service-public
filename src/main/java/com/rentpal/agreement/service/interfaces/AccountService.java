package com.rentpal.agreement.service.interfaces;/*
 * @author frank
 * @created 14 Dec,2020 - 11:59 PM
 */

import com.rentpal.agreement.model.User;

public interface AccountService {
    void addUser(User user);

    boolean userExist(String email);
}
