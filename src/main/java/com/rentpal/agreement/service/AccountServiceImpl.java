package com.rentpal.agreement.service;

import com.rentpal.agreement.model.User;
import com.rentpal.agreement.repository.UserRepository;
import com.rentpal.agreement.service.interfaces.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 *
 * @author frank
 * @created 14 Dec,2020 - 11:59 PM
 * Service class that adds user to database if not exists.
 */
@Transactional
@Service
public class AccountServiceImpl implements AccountService {

    private final UserRepository userRepository;

    /**
     * Instantiates a new Account service.
     *
     * @param userRepository the user repository
     */
    @Autowired
    public AccountServiceImpl (UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Override
    public void addUser(User user){
        userRepository.save(user);
    }

    @Override
    public boolean userExist(String email){
        return userRepository.existsByEmail(email);
    }
}
