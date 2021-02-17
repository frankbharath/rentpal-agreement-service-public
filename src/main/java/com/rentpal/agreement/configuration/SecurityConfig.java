package com.rentpal.agreement.configuration;

import com.rentpal.agreement.filters.CsrfHeaderFilter;
import com.rentpal.agreement.service.CustomOAuth2Service;
import com.rentpal.agreement.service.CustomOidcUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author frank
 * @created 08 Feb,2021 - 5:40 PM
 */

@EnableWebSecurity
@Configuration
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Value("${angular}")
    private String angularUrl;

    private final CustomOidcUserService customOidcUserService;

    private final CustomOAuth2Service customOAuth2Service;

    @Autowired
    public SecurityConfig(CustomOidcUserService customOidcUserService, CustomOAuth2Service customOAuth2Service) {
        this.customOidcUserService = customOidcUserService;
        this.customOAuth2Service = customOAuth2Service;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
            .exceptionHandling()
                .authenticationEntryPoint((HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e)-> {
                    httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                })
                .accessDeniedHandler((HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e)->{
                    httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
                })
        /*.and()
            .csrf()
                .csrfTokenRepository(csrfTokenRepository())*/
        .and()
            .csrf().disable()
            .cors()
        .and()
            .addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
            .authorizeRequests()
                .antMatchers(new String[] {"/user/session",
                        "/oauth2/**", "/login/**", "/api/login/**"}).permitAll()
            .anyRequest().authenticated()
        .and()
            .oauth2Login()
                .authorizationEndpoint()
                //.baseUri("/oauth2/authorization/*")
            .and()
                .redirectionEndpoint()
                //.baseUri("/oauth2/callback/*")
            .and()
                .userInfoEndpoint()
                .oidcUserService(customOidcUserService)
                .userService(customOAuth2Service)
            .and()
                .successHandler((HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) -> httpServletResponse.sendRedirect(angularUrl))
        .and()
            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .addLogoutHandler((HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication)->{
                    try {
                        httpServletResponse.sendRedirect(angularUrl+"/login");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    /**
     * CORS configuration for angular web application.
     *
     * @return the cors configuration source
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(angularUrl));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH", "OPTIONS"));
        configuration.applyPermitDefaultValues();
        configuration.setAllowCredentials(true);
        //the below three lines will add the relevant CORS response headers
        configuration.addAllowedOrigin(angularUrl);
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addExposedHeader("X-Total-Count");
        configuration.addExposedHeader("X-XSRF-TOKEN");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Csrf token repository that will set csrf token in the header. AngularJS will read the csrf token and add the csrf token to all the
     * requests
     * @return the csrf token repository
     */
    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }
}
