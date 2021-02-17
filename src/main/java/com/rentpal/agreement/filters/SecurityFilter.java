package com.rentpal.agreement.filters;

import com.rentpal.agreement.common.RentpalThreadLocal;
import com.rentpal.agreement.configuration.UserSession;
import com.rentpal.agreement.model.User;
import com.rentpal.agreement.service.interfaces.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author frank
 * @created 14 Dec,2020 - 8:31 PM
 */
@WebFilter
public class SecurityFilter implements Filter {

    private final AccountService accountService;

    /** The user session. */
    private final UserSession userSession;

    /**
     * Instantiates a new security filter.
     *
     * @param userSession the user session
     */
    @Autowired
    public SecurityFilter(AccountService accountService, UserSession userSession) {
        this.accountService=accountService;
        this.userSession=userSession;
    }

    /**
     * Filter that adds email and user id to the threadlocal and creates an account if not exists
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if(userSession.isAuthenticated()){
            RentpalThreadLocal.init();
            OAuth2User oAuth2User=(OAuth2User) userSession.getLoggedInUserDetails();
            String email=oAuth2User.getAttributes().get("email").toString();
            RentpalThreadLocal.add("email", email);
            RentpalThreadLocal.add("id", accountService.getUserId(email));
        }
        filterChain.doFilter(servletRequest, servletResponse);
        if(userSession.isAuthenticated()){
            RentpalThreadLocal.clear();
        }
    }
}
