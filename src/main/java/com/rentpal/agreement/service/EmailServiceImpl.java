package com.rentpal.agreement.service;

import com.rentpal.agreement.model.Mail;
import com.rentpal.agreement.service.interfaces.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

/**
 * The Class EmailServiceImpl.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Jul 12, 2020 1:49:10 AM
 */

@Component
public class EmailServiceImpl implements EmailService {
	
	/** The java mail sender. */
	private final JavaMailSender javaMailSender;

    /** The template engine. */
    private final SpringTemplateEngine templateEngine;

	/** The Environment variable to read application properties.*/
	private Environment env;
	
	/**
	 * Instantiates a new email service impl.
	 *
	 * @param javaMailSender the java mail sender
	 * @param templateEngine the template engine
	 */
	@Autowired
	public EmailServiceImpl(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine, Environment env) {
		this.javaMailSender=javaMailSender;
		this.templateEngine=templateEngine;
		this.env=env;
	}
	
	/**
	 * Send email.
	 *
	 * @param mail the mail
	 * @throws MessagingException the messaging exception
	 */
	public void sendEmail(Mail mail) throws MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		mail.setFrom(getSupportEmail());
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
	        
        helper.setTo(mail.getTo());
        if(mail.getTemplate()!=null) {
        	Context context = new Context();
        	if(mail.getModel()!=null) {
                context.setVariables(mail.getModel());
            }
        	String html = templateEngine.process(mail.getTemplate(), context);
        	helper.setText(html, true);
        }
        helper.setSubject(mail.getSubject());
        helper.setFrom(mail.getFrom());
        javaMailSender.send(message);
    }
	
	/**
	 * Gets the support email.
	 *
	 * @return the support email
	 */
	public String getSupportEmail() {
		return env.getProperty("spring.mail.username");
	}
	
}
