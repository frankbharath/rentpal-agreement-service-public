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
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author frank
 * @created 23 Dec,2020 - 9:27 PM
 */

@Slf4j
@Service
@Transactional
public class TenantServiceImpl implements TenantService {

    private final PropertyService propertyService;

    private final UnitService unitService;

    //private final AccountService accountService;

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
        //this.accountService=accountService;
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
     * @param tenantDTO     the tenantDTO tenantDTO
     */
    @Override
    public TenantDTO addTenant(TenantDTO tenantDTO){
        Long propertyId=tenantDTO.getPropertyId();
        Long unitId=tenantDTO.getUnitId();
        Property property=new Property();
        property.setId(propertyId);
        if(!unitService.unitExistsForProperty(propertyId, unitId)){
            throw new APIRequestException(Utils.getMessage(messageSource, "error.unit.not.exists"));
        }
        Unit unit=unitService.getUnitForProperty(propertyId, unitId);

        Tenant tenant= null;
        try {
            tenant = DTOModelMapper.tenantDTOModelMapper(tenantDTO);
            // check if the lease data is valid or not
            if(Utils.diffDays(tenant.getMovein().getTime(), tenant.getMoveout().getTime())< Constants.MINDAYS) {
                throw new APIRequestException(Utils.getMessage(messageSource, "error.invalid.date"));
            }
        } catch (ParseException e) {
            throw new APIRequestException(Utils.getMessage(messageSource,"error.invalid.date"));
        }
        tenant.setUnit(unit);
        // check if tenant exists for the given unit
        if(tenantRepository.existsByFirstNameAndLastNameAndEmailAndUnit(tenant.getFirstName(), tenant.getLastName(), tenant.getEmail(), tenant.getUnit())){
            throw new APIRequestException(Utils.getMessage(messageSource, "error.tenant.exists"));
        }

        User user=new User();
        user.setId(Utils.getUserId());
        tenant.setUser(user);
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
        //rabbitTemplate.convertAndSend(rabbitMQ.getExchange(), rabbitMQ.getRoutingKey(), mail);
        log.info("Adding tenant for the unit {} and property {}", tenant.getUnit().getId(), tenant.getUnit().getProperty().getId());
        /*for(int i=0;i<100;i++){
            Tenant tenant1=new Tenant();
            tenant1.setUser(user);
            tenant1.setOccupants(tenant.getOccupants());
            tenant1.setNationality(tenant.getNationality());
            tenant1.setLastName(tenant.getLastName());
            tenant1.setFirstName(tenant.getFirstName());
            tenant1.setEmail(tenant.getEmail()+i);
            tenant1.setDob(tenant.getDob());
            tenant1.setUnit(tenant.getUnit());
            tenant1.setMoveout(tenant.getMoveout());
            tenant1.setMovein(tenant.getMovein());
            tenantRepository.save(tenant1);
        }*/
        return DTOModelMapper.tenantModelDTOMapper(tenantRepository.save(tenant), messageSource);
    }

    @Override
    public void deleteTenant(Long tenantId){
        User user=new User();
        user.setId(Utils.getUserId());
        tenantRepository.deleteByIdAndUser(tenantId, user);
    }

    @Override
    public List<Tenant> getAllTenants(Integer page, Integer size){
        User user=new User();
        user.setId(Utils.getUserId());
        Pageable paging = PageRequest.of(page, size==null?Integer.MAX_VALUE:size);
        return tenantRepository.findAllByUser(user, paging);
    }

    @Override
    public JSONObject getRentHistory(){
        JSONObject response=new JSONObject();
        DateTime datetime = new DateTime(new Date());
        int month = Integer.parseInt(datetime.toString("MM"))-1;
        int totalMonths=Constants.TOTAL_MONTHS;
        int startIndex = (((month - Constants.RENT_HISTORY)%totalMonths+totalMonths)%totalMonths);
        List<Tenant> tenants=tenantRepository.findAllByMoveinBetweenAndUser(Utils.getUserId());
        Map<String, Float> map = new LinkedHashMap<>();
        int i=0;
        while(i++<Constants.RENT_HISTORY){
            map.put(Constants.MONTHS[startIndex],0f);
            startIndex=((++startIndex)%totalMonths+totalMonths)%totalMonths;
        }
        for(Tenant tenant:tenants){
            DateTime tenantStartDate=new DateTime(tenant.getMovein());
            int tenantStartMonth=Integer.parseInt(tenantStartDate.toString("MM"))-1;
            int diff=Months.monthsBetween(tenantStartDate, datetime).getMonths();
            while(diff>0){
                String monthStr=Constants.MONTHS[tenantStartMonth];
                map.computeIfPresent(monthStr,  (k, v) -> v + tenant.getUnit().getRent());
                tenantStartMonth=((++tenantStartMonth)%totalMonths+totalMonths)%totalMonths;
                diff--;
            }
        }

        List<JSONObject> mapList=map.keySet().stream().map(key->{
           JSONObject object=new JSONObject();
           object.put("month", key);
           object.put("rent", map.get(key));
           return object;
        }).collect(Collectors.toList());
        response.put("monthSummary", mapList);
        List<PropertyTenantRentInfo> infos=tenantRepository.findPropertyRentInfo(Utils.getUserId());
        response.put("propertySummary", infos.stream().map(value->{
            JSONObject object=new JSONObject();
            object.put("totalRent", value.getTotalRent());
            object.put("propertyName", value.getPropertyName());
            object.put("propertyId", value.getPropertyId());
            return object;
        }).collect(Collectors.toList()));
        List<UpcomingPayments> paymentInfos=tenantRepository.findUpcomingPayments(Utils.getUserId());
        response.put("upcomingPayments", paymentInfos.stream().map(value->{
            JSONObject object=new JSONObject();
            object.put("firstName", value.getFirstName());
            object.put("lastName", value.getLastName());
            object.put("email", value.getEmail());
            object.put("nextPayment", Utils.getDate(value.getNextPayment()));
            return object;
        }).collect(Collectors.toList()));
        return response;
    }

    @Override
    public Long getAllTenantsCount(){
        User user=new User();
        user.setId(Utils.getUserId());
        return tenantRepository.countByUser(user);
    }
}
