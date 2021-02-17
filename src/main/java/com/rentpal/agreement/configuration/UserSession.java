package com.rentpal.agreement.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * The Class UserSession.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Sep 15, 2020 3:42:16 PM
 */

@Configuration
public class UserSession {
	
	/**
	 * Checks if is authenticated.
	 *
	 * @return true, if is authenticated
	 */
	public boolean isAuthenticated() {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
	        return false;
	    }
	    return authentication.isAuthenticated();
	}
    
    /**
     * Gets the logged in user details.
     *
     * @return the logged in user details
     */
    public Object getLoggedInUserDetails() {
		return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
    
}
