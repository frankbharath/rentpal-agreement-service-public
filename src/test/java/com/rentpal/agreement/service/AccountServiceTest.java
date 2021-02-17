package com.rentpal.agreement.service;

import com.rentpal.agreement.AbstractTest;
import com.rentpal.agreement.common.Utils;
import com.rentpal.agreement.model.User;
import com.rentpal.agreement.repository.UserRepository;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * The type Account service test.
 *
 * @author frank
 * @created 24 Dec,2020 - 1:11 AM
 */

@PrepareForTest({AccountServiceImpl.class})
public class AccountServiceTest extends AbstractTest {

    /**
     * Unit test for the class account service.
     */
    @InjectMocks
    AccountServiceImpl accountService;

    /**
     * Mock bean injected for the account service class.
     */
    @Mock
    UserRepository userRepository;

    /**
     * Tests whether added user is being return or not.
     */
    @Test
    public void testAddUser(){
        User user=mock(User.class);
        when(userRepository.save(user)).thenReturn(user);
        assertTrue(accountService.addUser(user) instanceof User);
    }

    /**
     * Tests user exists or not.
     */
    @Test
    public void testUserExist(){
        when(userRepository.existsByEmail(Utils.getUserEmail())).thenReturn(true);
        assertTrue(accountService.userExist(Utils.getUserEmail()));
    }
}
