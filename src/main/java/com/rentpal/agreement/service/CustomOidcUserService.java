package com.rentpal.agreement.service;

import com.rentpal.agreement.model.User;
import com.rentpal.agreement.service.interfaces.AccountService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

/**
 * @author frank
 * @created 05 Dec,2020 - 1:54 AM
 * This class will register users who are logged in via google. Google uses openid connect for authentication
 */

@Service
public class CustomOidcUserService extends OidcUserService {

    private final AccountService accountService;

    /**
     * Creates a user account without password.
     *
     * @param accountService the account service
     */
    public CustomOidcUserService(AccountService accountService){
        this.accountService=accountService;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest){
        OidcUser oidcUser = super.loadUser(userRequest);
        String email=oidcUser.getAttribute("email").toString().toLowerCase();
        accountService.addUser(email);
        return oidcUser;
    }

}
