package com.rentpal.agreement.service.interfaces;


import com.rentpal.agreement.model.Mail;

import javax.mail.MessagingException;

/**
 * The Interface EmailService.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Jul 12, 2020 10:54:44 PM
 */

public interface EmailService {

	/** The Constant VERIFICAIONTEMPLATE. */
	String TENANT_CONFIRMATION_TEMPLATE="tenantconfirmation.html";
	/**
	 * Send email.
	 *
	 * @param mail the mail
	 * @throws MessagingException the messaging exception
	 */
	void sendEmail(Mail mail) throws MessagingException;

}
