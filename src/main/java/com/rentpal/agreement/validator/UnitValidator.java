package com.rentpal.agreement.validator;

import com.rentpal.agreement.common.Regex;
import com.rentpal.agreement.common.Utils;
import com.rentpal.agreement.model.Unit;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author frank
 * @created 16 Dec,2020 - 2:26 AM
 * Validates incoming unit object
 */
@Component
public class UnitValidator implements Validator {

    private final MessageSource messageSource;

    public UnitValidator(MessageSource messageSource){
        this.messageSource=messageSource;
    }

    @Override
    public boolean supports(Class unitClass) {
        return Unit.class.equals(unitClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "area", Utils.getMessage(messageSource,"error.invalid.parameter.mandatory", "area"));
        ValidationUtils.rejectIfEmpty(errors, "bathrooms", Utils.getMessage(messageSource,"error.invalid.parameter.mandatory", "bathrooms"));
        ValidationUtils.rejectIfEmpty(errors, "cautionDeposit", Utils.getMessage(messageSource,"error.invalid.parameter.mandatory", "cautionDeposit"));
        ValidationUtils.rejectIfEmpty(errors, "bedrooms", Utils.getMessage(messageSource,"error.invalid.parameter.mandatory", "bedrooms"));
        ValidationUtils.rejectIfEmpty(errors, "doorNumber", Utils.getMessage(messageSource,"error.invalid.parameter.mandatory", "doorNumber"));
        ValidationUtils.rejectIfEmpty(errors, "floorNumber", Utils.getMessage(messageSource,"error.invalid.parameter.mandatory", "floorNumber"));
        ValidationUtils.rejectIfEmpty(errors, "furnished", Utils.getMessage(messageSource,"error.invalid.parameter.mandatory", "furnished"));
        ValidationUtils.rejectIfEmpty(errors, "rent", Utils.getMessage(messageSource,"error.invalid.parameter.mandatory", "rent"));

        Unit unit = (Unit) o;
        if(unit.getArea()!=null){
            if(!Regex.FLOAT.matcher(String.valueOf(unit.getArea())).matches()){
                errors.rejectValue("area",Utils.getMessage(messageSource,"error.invalid.parameter", "area"));
            }
        }
        if(unit.getRent()!=null){
            if(!Regex.FLOAT.matcher(String.valueOf(unit.getRent())).matches()){
                errors.rejectValue("rent",Utils.getMessage(messageSource,"error.invalid.parameter", "rent"));
            }
        }
        if(unit.getCautionDeposit()!=null){
            if(!Regex.FLOAT.matcher(String.valueOf(unit.getCautionDeposit())).matches()){
                errors.rejectValue("cautionDeposit",Utils.getMessage(messageSource,"error.invalid.parameter", "cautionDeposit"));
            }
        }
        if(unit.getBathrooms()!=null){
            if(!Regex.INTEGER.matcher(String.valueOf(unit.getBathrooms())).matches()){
                errors.rejectValue("bathrooms",Utils.getMessage(messageSource,"error.invalid.parameter", "bathrooms"));
            }
        }
        if(unit.getFloorNumber()!=null){
            if(!Regex.INTEGER.matcher(String.valueOf(unit.getFloorNumber())).matches()){
                errors.rejectValue("floorNumber",Utils.getMessage(messageSource,"error.invalid.parameter", "floorNumber"));
            }
        }
        if(unit.getBedrooms()!=null){
            if(!Regex.INTEGER.matcher(String.valueOf(unit.getBedrooms())).matches()){
                errors.rejectValue("bedrooms",Utils.getMessage(messageSource,"error.invalid.parameter", "bedrooms"));
            }
        }
        if(unit.getDoorNumber()!=null){
            if(!Regex.ALPHA_NUM.matcher(String.valueOf(unit.getDoorNumber())).matches()){
                errors.rejectValue("doorNumber",Utils.getMessage(messageSource,"error.invalid.parameter", "doorNumber"));
            }
            if(unit.getDoorNumber().length()>4){
                errors.rejectValue("doorNumber",Utils.getMessage(messageSource,"error.invalid.parameter.max.length", "doorNumber", 4));
            }
        }
        if(unit.getFurnished()!=null){
            if(!Regex.BOOLEAN.matcher(unit.getFurnished().toString()).matches()){
                errors.rejectValue("furnished",Utils.getMessage(messageSource,"error.invalid.parameter", "furnished"));
            }
        }
    }
}
