package com.rentpal.agreement.service;

import com.rentpal.agreement.common.RentpalThreadLocal;
import com.rentpal.agreement.common.Utils;
import com.rentpal.agreement.exception.APIRequestException;
import com.rentpal.agreement.model.User;
import com.rentpal.agreement.repository.UserRepository;
import com.rentpal.agreement.service.interfaces.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 *
 * @author frank
 * @created 14 Dec,2020 - 11:59 PM Service class that adds user to database if not exists.
 */
@Transactional
@Service
public class AccountServiceImpl implements AccountService {

    private final UserRepository userRepository;

    private final InitData initData;

    /**
     * For localization, I18N
     */
    private final MessageSource messageSource;

    /**
     * Instantiates a new Account service.
     *
     * @param userRepository the user repository
     * @param initData
     */
    @Autowired
    public AccountServiceImpl(UserRepository userRepository, InitData initData, MessageSource messageSource){
        this.userRepository=userRepository;
        this.initData = initData;
        this.messageSource=messageSource;
    }

    @Override
    public User addUser(User user){
        return userRepository.save(user);
    }

    @Override
    public boolean userExist(String email){
        return userRepository.existsByEmail(email);
    }

    @Override
    public Long getUserId(String email){ return userRepository.getUserId(email);}

    @Override
    public void addUser(String email){
        if(!userExist(email)){
            User user=new User();
            user.setEmail(email);
            user.setCreationTime(System.currentTimeMillis());
            RentpalThreadLocal.init();
            RentpalThreadLocal.add("email", email);
            user=addUser(user);
            RentpalThreadLocal.add("id", user.getId());
            initData.initData();
            RentpalThreadLocal.clear();
        }
    }

    @Override
    public User getUser(){
        return userRepository.findById(Utils.getUserId()).orElseThrow(()->{
            throw new APIRequestException(Utils.getMessage(messageSource,"error.user.not_exists"));
        });
    }

}
