package com.rentpal.agreement;

import com.rentpal.agreement.common.DTOModelMapper;
import com.rentpal.agreement.common.RentpalThreadLocal;
import com.rentpal.agreement.common.Utils;
import com.rentpal.agreement.model.User;
import org.apache.catalina.core.ApplicationContext;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Oct 15, 2020 7:09:03 PM
 	* Class Description
*/
@RunWith(PowerMockRunner.class)
@PrepareForTest({Utils.class, DTOModelMapper.class})
public abstract class AbstractTest {

	@Before
	public void setUp() {
		PowerMockito.mockStatic(Utils.class);
		PowerMockito.when(Utils.getUserId()).thenReturn(1l);
		PowerMockito.when(Utils.getUserEmail()).thenReturn("test@gmail.com");
		PowerMockito.mockStatic(DTOModelMapper.class);
	}
}
