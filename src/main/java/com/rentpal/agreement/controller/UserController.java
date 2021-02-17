package com.rentpal.agreement.controller;

/*
 * @author frank
 * @created 09 Feb,2021 - 5:36 PM
 */

import com.rentpal.agreement.common.DTOModelMapper;
import com.rentpal.agreement.configuration.UserSession;
import com.rentpal.agreement.service.interfaces.AccountService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final AccountService accountService;

    /** The user session. */
    private final UserSession userSession;

    UserController(AccountService accountService, UserSession userSession){
        this.accountService=accountService;
        this.userSession=userSession;
    }

    @GetMapping(value = "/user/session", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> userLoggedIn(){
        JSONObject object=new JSONObject();
        object.put("status",userSession.isAuthenticated());
        return new ResponseEntity<>(object.toString(), HttpStatus.OK);
    }

    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUser(){
        return new ResponseEntity<>(DTOModelMapper.userModelDTOMapper(accountService.getUser()), HttpStatus.OK);
    }
}
