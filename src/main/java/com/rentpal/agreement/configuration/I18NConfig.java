package com.rentpal.agreement.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

/**
 * The Class I18NConfig.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Jun 23, 2020 7:25:31 PM
 * This class provides configuration for I18N
 */
@Configuration
public class I18NConfig implements WebMvcConfigurer  {

	/**
	 * Message source.
	 *
	 * @return the message source
	 */
	@Bean("messageSource")
	public MessageSource messageSource() {
	    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
	    messageSource.setBasenames("i18n/messages","i18n/countries");
	    messageSource.setDefaultEncoding("UTF-8");
	    return messageSource;
	}
	
	/**
	 * Locale resolver.
	 *
	 * @return the locale resolver
	 */
	@Bean(name = "localeResolver")
	public LocaleResolver localeResolver() {
		CookieLocaleResolver clr = new CookieLocaleResolver();
		clr.setDefaultLocale(Locale.US);
	    return clr;
	}
	
	/**
	 * Adds the interceptors.
	 *
	 * @param registry the registry
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
	    localeChangeInterceptor.setParamName("locale");
	    localeChangeInterceptor.setIgnoreInvalidLocale(true);
	    registry.addInterceptor(localeChangeInterceptor);
	 }
}
