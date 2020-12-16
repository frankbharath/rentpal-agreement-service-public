package com.rentpal.agreement.filters;

/*
 * @author frank
 * @created 14 Dec,2020 - 8:31 PM
 */

import com.rentpal.agreement.common.RentpalThreadLocal;
import com.rentpal.agreement.model.User;
import com.rentpal.agreement.service.interfaces.AccountService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter
public class SecurityFilter implements Filter {

    @Autowired
    private AccountService accountService;

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
