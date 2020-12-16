package com.rentpal.agreement.exception;

import com.rentpal.agreement.common.Constants;
import com.rentpal.agreement.common.Utils;
import com.rentpal.agreement.model.APIException;
import com.rentpal.agreement.model.InvalidField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * @author frank
 * @created 14 Dec,2020 - 9:36 PM
 */

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @Autowired
    public GlobalExceptionHandler(MessageSource messageSource){
        this.messageSource=messageSource;
    }

    /**
     * Handle API request exception.
     *
     * @param exception        the exception
     * @throws IOException      Signals that an I/O exception has occurred.
     * @throws ServletException the servlet exception
     * @return
     */
    @ExceptionHandler(value= {APIRequestException.class})
    public ResponseEntity<APIException> handleAPIRequestException(APIRequestException exception){
        log.error(exception.getMessage(), exception);
        APIException apiException=new APIException();
        apiException.setMessage(exception.getMessage());
        apiException.setStatus(Constants.FAILED);
        apiException.setTimestamp(Utils.getDate(System.currentTimeMillis()));
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value= {MethodArgumentTypeMismatchException.class})
    public ResponseEntity<APIException> handleInvalidParameter(MethodArgumentTypeMismatchException exception){
        log.error(exception.getMessage(), exception);
        String fieldName= exception.getParameter().getParameterName();
        InvalidField invalidField=new InvalidField();
        invalidField.setField(fieldName);
        invalidField.setMessage(Utils.getMessage(messageSource, "error.invalid.parameter", fieldName));
        return new ResponseEntity<>(getAPIExceptionForInvalidFields(invalidField), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Handle bind exception response entity.
     *
     * @param ex the ex
     * @return the response entity
     */
    @ResponseBody
    @ExceptionHandler(value = {BindException.class})
    public ResponseEntity<Object> handleBindException(BindException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        List<InvalidField> invalidFields =new ArrayList<>();
        for (org.springframework.validation.FieldError fieldError: fieldErrors) {
            InvalidField invalidField =new InvalidField();
            String message=fieldError.getCode();
            invalidField.setMessage(message);
            invalidField.setField(fieldError.getField());
            invalidFields.add(invalidField);
        }
        return new ResponseEntity<>(getAPIExceptionForInvalidFields(invalidFields), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private APIException getAPIExceptionForInvalidFields(InvalidField invalidField){
        List<InvalidField> invalidFields = new ArrayList<>();
        invalidFields.add(invalidField);
        return getAPIExceptionForInvalidFields(invalidFields);
    }
    private APIException getAPIExceptionForInvalidFields(List<InvalidField> invalidFields){
        APIException exception=new APIException();
        exception.setMessage(invalidFields);
        exception.setStatus(Constants.FAILED);
        exception.setTimestamp(Utils.getDate(System.currentTimeMillis()));
        return exception;
    }
}
