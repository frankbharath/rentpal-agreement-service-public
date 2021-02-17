package com.rentpal.agreement.validator;



import com.rentpal.agreement.common.Regex;
import com.rentpal.agreement.common.Utils;
import com.rentpal.agreement.dto.PropertyDTO;
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

    private static final String[] PROPERTY_NAME={"propertyname", "Property name"};
    private static final String[] ADDRESSLINE_1={"addressline_1", "Address line 1"};
    private static final String[] ADDRESSLINE_2={"addressline_2", "Address line 2"};
    private static final String[] POSTAL={"postal", "Postal"};
    private static final String[] CITY={"city", "City"};

    @Autowired
    public PropertyValidator(MessageSource messageSource){
        this.messageSource=messageSource;
    }

    @Override
    public boolean supports(Class propertyClass) {
        return PropertyDTO.class.equals(propertyClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, PROPERTY_NAME[0], Utils.getMessage(messageSource,"error.invalid.parameter.mandatory", PROPERTY_NAME[1]));
        ValidationUtils.rejectIfEmpty(errors, ADDRESSLINE_1[0], Utils.getMessage(messageSource,"error.invalid.parameter.mandatory", ADDRESSLINE_1[1]));
        ValidationUtils.rejectIfEmpty(errors, CITY[0], Utils.getMessage(messageSource,"error.invalid.parameter.mandatory", CITY[1]));
        ValidationUtils.rejectIfEmpty(errors, POSTAL[0], Utils.getMessage(messageSource,"error.invalid.parameter.mandatory", POSTAL[1]));
        PropertyDTO property = (PropertyDTO) o;
        if(property.getPropertyname()!=null){
            if(!Regex.NOT_ALLOWED_CHARACTERS.matcher(property.getPropertyname()).matches()){
                errors.rejectValue(PROPERTY_NAME[0],Utils.getMessage(messageSource,"error.invalid.parameter", PROPERTY_NAME[1]));
            }
            if(property.getPropertyname().length()>64){
                errors.rejectValue(PROPERTY_NAME[0],Utils.getMessage(messageSource,"error.invalid.parameter.max.length", PROPERTY_NAME[1], 64));
            }
        }
        if(property.getAddressline_1()!=null){
            if(!Regex.NOT_ALLOWED_CHARACTERS.matcher(property.getAddressline_1()).matches()){
                errors.rejectValue(ADDRESSLINE_1[0],Utils.getMessage(messageSource,"error.invalid.parameter", ADDRESSLINE_1[1]));
            }
            if(property.getAddressline_1().length()>128){
                errors.rejectValue(ADDRESSLINE_1[0],Utils.getMessage(messageSource,"error.invalid.parameter.max.length", ADDRESSLINE_1[1], 128));
            }
        }
        if(property.getAddressline_2()!=null && property.getAddressline_2().trim().length()>0){
            if(!Regex.NOT_ALLOWED_CHARACTERS.matcher(property.getAddressline_2()).matches()){
                errors.rejectValue(ADDRESSLINE_2[0],Utils.getMessage(messageSource,"error.invalid.parameter", ADDRESSLINE_2[1]));
            }
            if(property.getAddressline_2().length()>128){
                errors.rejectValue(ADDRESSLINE_2[0], Utils.getMessage(messageSource,"error.invalid.parameter.max.length", ADDRESSLINE_2[1], 128));
            }
        }
        if(property.getPostal()!=null){
            if(!Regex.POSTAL_CODE.matcher(property.getPostal()).matches()){
                errors.rejectValue(POSTAL[0],Utils.getMessage(messageSource,"error.invalid.parameter", POSTAL[1]));
            }
        }
        if(property.getCity()!=null){
            if(!Regex.NOT_ALLOWED_CHARACTERS.matcher(property.getCity()).matches()){
                errors.rejectValue(CITY[0],Utils.getMessage(messageSource,"error.invalid.parameter", CITY[1]));
            }
            if(property.getCity().length()>64){
                errors.rejectValue(CITY[0],Utils.getMessage(messageSource,"error.invalid.parameter.max.length", CITY[1], 64));
            }
        }
    }
}
