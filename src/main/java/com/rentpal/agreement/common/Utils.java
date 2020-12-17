package com.rentpal.agreement.common;

import com.rentpal.agreement.model.APIRequestResponse;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author frank
 * @created 12 Dec,2020 - 3:24 AM
 *
 * Utility class holding common functions
 */
public class Utils {
    /**
     * Gets the api request response.
     *
     * @param data the data
     * @return the api request response
     */
    public static APIRequestResponse getApiRequestResponse(final Object data) {
        APIRequestResponse response=new APIRequestResponse();
        if(data!=null) {
            response.setData(data);
        }
        //response.setMessage(message);
        //response.setHttpStatus(HttpStatus.OK);
        //response.setStatus(Constants.SUCCESS);
        return response;
    }

    /**
     * Gets the date for given milliseconds.
     *
     * @param milliseconds the milliseconds
     * @return the date
     */
    public static String getDate(Long milliseconds) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH);
        return simpleDateFormat.format(new Date(milliseconds));
    }

    /**
     * Gets the user id.
     *
     * @return the user id
     */
    public static Long getUserId() {
        Object id=RentpalThreadLocal.get("id");
        if(id!=null){
            return Long.parseLong(id.toString());
        }
        return null;
    }

    /**
     * Gets the user email.
     *
     * @return the user email
     */
    public static String getUserEmail() {
        Object email=RentpalThreadLocal.get("email");
        if(email!=null){
            return RentpalThreadLocal.get("email").toString();
        }
        return null;
    }

    /**
     * Gets the localized message string.
     *
     * @param messageSource the message source
     * @param key           the key
     * @return the string
     */
    public static String getMessage(final MessageSource messageSource, final String key){
        return getMessage(messageSource, key, null);
    }

    /**
     * Gets the localized message string for parameters.
     *
     * @param messageSource the message source
     * @param key           the key
     * @param args          the args
     * @return the string
     */
    public static String getMessage(final MessageSource messageSource, final String key, final Object ...args){
        String message=key;
        try{
            message=messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
        }catch (Exception exception){ }
        return message;
    }

}
