package com.rentpal.agreement.validator;



import com.rentpal.agreement.common.Regex;
import com.rentpal.agreement.common.Utils;
import com.rentpal.agreement.model.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author frank
 * @created 16 Dec,2020 - 1:32 AM
 * Validates incoming property object
 */
@Component
public class PropertyValidator implements Validator {

    private final MessageSource messageSource;

    @Autowired
    public PropertyValidator(MessageSource messageSource){
        this.messageSource=messageSource;
    }

    @Override
    public boolean supports(Class propertyClass) {
        return Property.class.equals(propertyClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "propertyName", Utils.getMessage(messageSource,"error.invalid.parameter.mandatory", "propertyName"));
        ValidationUtils.rejectIfEmpty(errors, "addressLine1", Utils.getMessage(messageSource,"error.invalid.parameter.mandatory", "addressLine1"));
        ValidationUtils.rejectIfEmpty(errors, "city", Utils.getMessage(messageSource,"error.invalid.parameter.mandatory", "city"));
        ValidationUtils.rejectIfEmpty(errors, "postal", Utils.getMessage(messageSource,"error.invalid.parameter.mandatory", "postal"));
        Property property = (Property) o;
        if(property.getPropertyName()!=null){
            if(!Regex.NOT_ALLOWED_CHARACTERS.matcher(property.getPropertyName()).matches()){
                errors.rejectValue("propertyName",Utils.getMessage(messageSource,"error.invalid.parameter", "propertyName"));
            }
            if(property.getPropertyName().length()>64){
                errors.rejectValue("propertyName",Utils.getMessage(messageSource,"error.invalid.parameter.max.length", "propertyName", 64));
            }
        }
        if(property.getAddressLine1()!=null){
            if(!Regex.NOT_ALLOWED_CHARACTERS.matcher(property.getAddressLine1()).matches()){
                errors.rejectValue("addressLine1",Utils.getMessage(messageSource,"error.invalid.parameter", "addressLine1"));
            }
            if(property.getAddressLine1().length()>128){
                errors.rejectValue("addressLine1",Utils.getMessage(messageSource,"error.invalid.parameter.max.length", "addressLine1", 128));
            }
        }
        if(property.getAddressLine2()!=null){
            if(!Regex.NOT_ALLOWED_CHARACTERS.matcher(property.getAddressLine2()).matches()){
                errors.rejectValue("addressLine2",Utils.getMessage(messageSource,"error.invalid.parameter", "addressLine2"));
            }
            if(property.getAddressLine2().length()>128){
                errors.rejectValue("addressLine2",Utils.getMessage(messageSource,"error.invalid.parameter.max.length", "addressLine2", 128));
            }
        }
        if(property.getPostal()!=null){
            if(!Regex.POSTAL_CODE.matcher(property.getPostal()).matches()){
                errors.rejectValue("postal",Utils.getMessage(messageSource,"error.invalid.parameter", "postal"));
            }
        }
        if(property.getCity()!=null){
            if(!Regex.NOT_ALLOWED_CHARACTERS.matcher(property.getCity()).matches()){
                errors.rejectValue("city",Utils.getMessage(messageSource,"error.invalid.parameter", "city"));
            }
            if(property.getCity().length()>64){
                errors.rejectValue("city",Utils.getMessage(messageSource,"error.invalid.parameter.max.length", "city", 64));
            }
        }
    }
}
