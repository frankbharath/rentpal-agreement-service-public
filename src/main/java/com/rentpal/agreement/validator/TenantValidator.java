package com.rentpal.agreement.validator;

import com.rentpal.agreement.common.Regex;
import com.rentpal.agreement.common.Utils;
import com.rentpal.agreement.dto.TenantDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author frank
 * @created 03 Feb,2021 - 12:46 PM
 */

@Component
public class TenantValidator implements Validator {

    private final MessageSource messageSource;

    private static final String[][] COLUMNS ={
        {"firstName", "First Name"},
        {"lastName", "Last Name"},
        {"email", "Email"},
        {"dob", "Date of birth"},
        {"nationality", "Nationality"},
        {"unitId", "Unit id"},
        {"propertyId", "Property id"},
        {"movein", "Move in"},
        {"moveout", "Move out"},
        {"occupants", "Occupants"},
    };

    @Autowired
    public TenantValidator(MessageSource messageSource){
        this.messageSource=messageSource;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return TenantDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        TenantDTO tenantDTO=(TenantDTO) o;
        for(String[] column:COLUMNS){
            ValidationUtils.rejectIfEmpty(errors, column[0], Utils.getMessage(messageSource,"error.invalid.parameter.mandatory", column[1]));
            switch (column[0]){
                case "firstName":
                    if(tenantDTO.getFirstName()!=null){
                        if(!Regex.ALPHAWITHSPACE.matcher(tenantDTO.getFirstName()).matches()){
                            errors.rejectValue(column[0],Utils.getMessage(messageSource,"error.invalid.parameter", column[1]));
                        }
                        if(tenantDTO.getFirstName().length()>255){
                            errors.rejectValue(column[0],Utils.getMessage(messageSource,"error.invalid.parameter.max.length", column[1], 255));
                        }
                    }
                break;
                case "lastName":
                    if(tenantDTO.getLastName()!=null){
                        if(!Regex.ALPHAWITHSPACE.matcher(tenantDTO.getLastName()).matches()){
                            errors.rejectValue(column[0],Utils.getMessage(messageSource,"error.invalid.parameter", column[1]));
                        }
                        if(tenantDTO.getLastName().length()>255){
                            errors.rejectValue(column[0],Utils.getMessage(messageSource,"error.invalid.parameter.max.length", column[1], 255));
                        }
                    }
                break;
                case "email":
                    if(tenantDTO.getEmail()!=null){
                        if(!Regex.EMAIL.matcher(tenantDTO.getEmail()).matches()){
                            errors.rejectValue(column[0],Utils.getMessage(messageSource,"error.invalid.parameter", column[1]));
                        }
                        if(tenantDTO.getEmail().length()>255){
                            errors.rejectValue(column[0],Utils.getMessage(messageSource,"error.invalid.parameter.max.length", column[1], 255));
                        }
                    }
                break;
                case "dob":
                    if(tenantDTO.getDob()!=null && !Regex.DATE.matcher(tenantDTO.getDob()).matches()){
                        errors.rejectValue(column[0],Utils.getMessage(messageSource,"error.invalid.parameter", column[1]));
                    }
                break;
                case "nationality":
                    if(tenantDTO.getNationality()!=null && !Regex.NATIONALITY.matcher(tenantDTO.getNationality()).matches()){
                        errors.rejectValue(column[0],Utils.getMessage(messageSource,"error.invalid.parameter", column[1]));
                    }
                break;
                case "movein":
                    if(tenantDTO.getMovein()!=null && !Regex.DATE.matcher(tenantDTO.getMovein()).matches()){
                        errors.rejectValue(column[0],Utils.getMessage(messageSource,"error.invalid.parameter", column[1]));
                    }
                break;
                case "moveout":
                    if(tenantDTO.getMoveout()!=null && !Regex.DATE.matcher(tenantDTO.getMoveout()).matches()){
                        errors.rejectValue(column[0],Utils.getMessage(messageSource,"error.invalid.parameter", column[1]));
                    }
                break;
                case "occupants":
                    if(tenantDTO.getOccupants()!=null && !Regex.INTEGER.matcher(String.valueOf(tenantDTO.getOccupants())).matches()){
                        errors.rejectValue(column[0],Utils.getMessage(messageSource,"error.invalid.parameter", column[1]));
                    }
                break;
            }
        }
    }
}
