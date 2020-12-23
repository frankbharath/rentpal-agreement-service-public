package com.rentpal.agreement.service;

import com.rentpal.agreement.common.Constants;
import com.rentpal.agreement.common.DTOModelMapper;
import com.rentpal.agreement.common.Utils;
import com.rentpal.agreement.dto.TenantDTO;
import com.rentpal.agreement.exception.APIRequestException;
import com.rentpal.agreement.model.*;
import com.rentpal.agreement.repository.TenantRepository;
import com.rentpal.agreement.service.interfaces.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author frank
 * @created 23 Dec,2020 - 9:27 PM
 */

@Slf4j
@Service
public class TenantServiceImpl implements TenantService {

    private final PropertyService propertyService;

    private final UnitService unitService;

    /**
     * Zoho sign integration to send digital rental agreements.
     */

    private final DigitalAgreementService digitalAgreementService;

    /**
     * Performs CRUD operations on tenant object.
     */
    private final TenantRepository tenantRepository;

    /**
     * For asynchronous communication
     */
    private final RabbitTemplate rabbitTemplate;

    /**
     * RabbitMQ configurations
     */
    private RabbitMQ rabbitMQ;

    /**
     * For localization, I18N
     */
    private final MessageSource messageSource;

    public TenantServiceImpl(TenantRepository tenantRepository, PropertyService propertyService, UnitService unitService, MessageSource messageSource, DigitalAgreementService digitalAgreementService, RabbitTemplate rabbitTemplate){
        this.tenantRepository=tenantRepository;
        this.propertyService=propertyService;
        this.unitService=unitService;
        this.messageSource=messageSource;
        this.digitalAgreementService=digitalAgreementService;
        this.rabbitTemplate=rabbitTemplate;
    }

    @Autowired
    private void setRabbitMQ(RabbitMQ rabbitMQ){
        this.rabbitMQ=rabbitMQ;
    }

    /**
     * Adds a tenant to a given property and sends a digital rental agreement.
     *
     * @param propertyId the property id
     * @param unitId     the unit id
     *  @param unitId     the tenantDTO tenantDTO
     */
    @Override
    public TenantDTO addTenant(Long propertyId, Long unitId, TenantDTO tenantDTO){
        Property property=new Property();
        property.setId(propertyId);
        // check if property and unit exist for the user
        if(!propertyService.propertyExistsForUser(propertyId)){
            throw new APIRequestException(Utils.getMessage(messageSource,"error.property.not_exists"));
        }
        if(!unitService.unitExistsForProperty(unitId, propertyId)){
            throw new APIRequestException(Utils.getMessage(messageSource, "error.unit.not.exists"));
        }
        Unit unit=new Unit();
        unit.setId(unitId);
        unit.setProperty(property);
        tenantDTO.setUnitId(unitId);
        Tenant tenant= null;
        try {
            tenant = DTOModelMapper.tenantDTOModelMapper(tenantDTO);
            // check if the lease data is valid or not
            Long currentDate = Utils.parseDateToMilliseconds(new SimpleDateFormat("MMM d, yyyy").format(new Date()));
            if(tenant.getMovein()<currentDate || Utils.diffDays(tenant.getMovein(), tenant.getMoveout())< Constants.MINDAYS) {
                throw new APIRequestException(Utils.getMessage(messageSource, "error.invalid.date"));
            }
        } catch (ParseException e) {
            throw new APIRequestException(Utils.getMessage(messageSource,"error.invalid.date"));
        }
        // check if tenant exists for the given unit
        if(tenantRepository.existsByFirstNameAndLastNameAndEmailAndUnit(tenant.getFirstName(), tenant.getLastName(), tenant.getEmail(), tenant.getUnit())){
            throw new APIRequestException(Utils.getMessage(messageSource, "error.tenant.exists"));
        }
        Unit unitDB=unitService.getUnitForProperty(propertyId, unitId);
        User user=new User();
        user.setId(Utils.getUserId());
        Property propertyDB=propertyService.getProperty(propertyId);
        tenant.getUnit().setProperty(propertyDB);
        //digitalAgreementService.createSignRequest(tenant, propertyDB, unitDB, 0);
        Mail mail=new Mail(tenant.getEmail(), Utils.getMessage(messageSource, "tenant.confirmation"), EmailService.TENANT_CONFIRMATION_TEMPLATE);
        Map<String,Object> model=new HashMap<>();
        model.put("user", tenant.getEmail().split("@")[0]);
        model.put("owner", Utils.getUserEmail());
        model.put("addressline1", propertyDB.getAddressLine1());
        model.put("addressline2", propertyDB.getAddressLine2());
        model.put("city", propertyDB.getPostal()+", "+propertyDB.getCity());
        mail.setModel(model);
        rabbitTemplate.convertAndSend(rabbitMQ.getExchange(), rabbitMQ.getRoutingKey(), mail);
        log.info("Adding tenant for the unit {} and property {}", tenant.getUnit().getId(), tenant.getUnit().getProperty().getId());
        return DTOModelMapper.tenantModelDTOMapper(tenantRepository.save(tenant), messageSource);
    }
}
