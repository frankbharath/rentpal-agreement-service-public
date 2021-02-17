package com.rentpal.agreement.service;

import com.rentpal.agreement.model.User;
import com.rentpal.agreement.service.interfaces.AccountService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * @author frank
 * @created 05 Dec,2020 - 2:25 AM
 * This class will register users who are logged in via facebook. facebook uses OAuth2.0 for authentication.
 */
@Service
public class CustomOAuth2Service extends DefaultOAuth2UserService {

    private final AccountService accountService;

    /**
     * Creates a user account without password.
     *
     * @param accountService the account service
     * @param accountService
     */
    public CustomOAuth2Service(AccountService accountService){
        this.accountService = accountService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest){
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String email=oAuth2User.getAttribute("email").toString().toLowerCase();
        accountService.addUser(email);
        return oAuth2User;
    }
}
