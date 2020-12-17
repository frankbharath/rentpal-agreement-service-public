package com.rentpal.agreement.filters;

import com.rentpal.agreement.common.RentpalThreadLocal;
import com.rentpal.agreement.model.User;
import com.rentpal.agreement.service.interfaces.AccountService;
import org.springframework.beans.factory.annotation.Autowired;

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

    @Autowired
    private AccountService accountService;

    /**
     * Filter that adds email and user id to the threadlocal and creates an account if not exists
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        RentpalThreadLocal.init();
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if(request.getHeader("email")!=null){
            RentpalThreadLocal.add("email", request.getHeader("email"));
        }
        if(request.getHeader("id")!=null){
            if(!accountService.userExist(request.getHeader("email"))){
                User user=new User();
                user.setId(Long.parseLong(request.getHeader("id")));
                user.setEmail(request.getHeader("email"));
                accountService.addUser(user);
            }
            RentpalThreadLocal.add("id", request.getHeader("id"));
        }
        filterChain.doFilter(servletRequest, servletResponse);
        RentpalThreadLocal.clear();
    }

}
