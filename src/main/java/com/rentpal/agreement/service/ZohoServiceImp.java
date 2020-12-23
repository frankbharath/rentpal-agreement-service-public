package com.rentpal.agreement.service;

import com.rentpal.agreement.common.Utils;
import com.rentpal.agreement.common.ZohoSignConstants;
import com.rentpal.agreement.exception.APIRequestException;
import com.rentpal.agreement.model.OAuthDetails;
import com.rentpal.agreement.model.Property;
import com.rentpal.agreement.model.Tenant;
import com.rentpal.agreement.model.Unit;
import com.rentpal.agreement.service.interfaces.DigitalAgreementService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * The Class ZohoServiceImp.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Sep 28, 2020 11:05:41 PM
 */

@Service
public class ZohoServiceImp implements DigitalAgreementService {

	/** The oauth details. */
	private OAuthDetails oAuthDetails;
	
	/** The access token. */
	private String accessToken;

	/** The Environment variable to read application properties.*/
	private Environment env;

	/**
	 * For localization, I18N
	 */
	private final MessageSource messageSource;
	
	/**
	 * Instantiates a new zoho service imp.
	 *
	 * @param oAuthDetails the o auth details
	 */
	@Autowired
	public ZohoServiceImp(OAuthDetails oAuthDetails, Environment env, MessageSource messageSource) {
		this.oAuthDetails=oAuthDetails;
		this.env=env;
		this.messageSource=messageSource;
	}
	
	/**
	 * Generate access token.
	 */
	@Override
	public void generateAccessToken() {
		this.accessToken=null;
		HttpPost postMethod = new HttpPost(env.getProperty("zoho.accounts.oauth")+"?client_id="+oAuthDetails.getClientId()+"&client_secret="+oAuthDetails.getClientSecret()+"&grant_type=refresh_token&refresh_token="+oAuthDetails.getRefreshToken());
		HttpResponse response;
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
			response = httpClient.execute(postMethod);
			String responseJSON = EntityUtils.toString(response.getEntity(), "UTF-8");
			JSONObject submitResponse = new JSONObject(responseJSON);
			if(submitResponse.has("access_token")) {
				this.accessToken=submitResponse.getString("access_token");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the request details.
	 *
	 * @param requestId the request id
	 * @return the request details
	 */
	@Override
	public JSONObject getRequestDetails(Long requestId) {
		HttpGet getMethod = new HttpGet(env.getProperty("zoho.sign.requests")+"/"+requestId);
		getMethod.setHeader("Authorization","Zoho-oauthtoken "+ accessToken);
		HttpResponse response;
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
			response = httpClient.execute(getMethod);
			String responseJSON = EntityUtils.toString(response.getEntity(), "UTF-8");
			JSONObject submitResponse = new JSONObject(responseJSON);
			return submitResponse;
		} catch (ClientProtocolException e) {
			throw new APIRequestException(Utils.getMessage(messageSource, "error.unkown_issue"));
		} catch (IOException e) {
			throw new APIRequestException(Utils.getMessage(messageSource, "error.unkown_issue"));
		}
	}
	
	/**
	 * Creates the sign request.
	 *
	 * @param tenant the tenant
	 * @param property the property
	 * @param unit the unit
	 * @param retry the retry
	 * @return the string
	 */
	@Override
	public String createSignRequest(Tenant tenant, Property property, Unit unit,  int retry) {
		JSONArray actions = new JSONArray();
		JSONObject actionJson = new JSONObject();
		//First recipient role - your internal user
		actionJson.put(ZohoSignConstants.ACTION_TYPE,"SIGN");
		actionJson.put(ZohoSignConstants.RECIPIENT_EMAIL,Utils.getUserEmail());
		actionJson.put(ZohoSignConstants.RECIPIENT_NAME,Utils.getUserEmail().split("@")[0]);
		actionJson.put(ZohoSignConstants.ACTION_ID,"16312000000011020");
		actionJson.put(ZohoSignConstants.VERIFY_RECIPIENT, false);
		actionJson.put(ZohoSignConstants.SIGNING_ORDER, 1);
		actionJson.put(ZohoSignConstants.ROLE, "owner");
		actions.put(actionJson);
		
		actionJson = new JSONObject();
		actionJson.put(ZohoSignConstants.ACTION_TYPE,"SIGN");
		actionJson.put(ZohoSignConstants.RECIPIENT_EMAIL, tenant.getEmail());
		actionJson.put(ZohoSignConstants.RECIPIENT_NAME, tenant.getFirstName()+" "+tenant.getLastName());
		actionJson.put(ZohoSignConstants.ACTION_ID,"16312000000011026");
		actionJson.put(ZohoSignConstants.VERIFY_RECIPIENT, false);
		actionJson.put(ZohoSignConstants.SIGNING_ORDER, 2);
		actionJson.put(ZohoSignConstants.ROLE, "tenant");
		actions.put(actionJson);
		
		
		JSONObject fieldTextData=new JSONObject();
		fieldTextData.put("addressline_1", property.getAddressLine1());
		fieldTextData.put("addressline_2", property.getAddressLine2());
		fieldTextData.put("city", property.getPostal()+", "+property.getCity());
		fieldTextData.put("area", unit.getArea()+" "+Utils.getMessage(messageSource,"lease.squarearea"));
		fieldTextData.put("firstname", tenant.getFirstName());
		fieldTextData.put("lastname", tenant.getLastName());
		fieldTextData.put("dob", tenant.getDob());
		fieldTextData.put("nationality", Utils.getMessage(messageSource, tenant.getNationality()));
		fieldTextData.put("movein", Utils.getDate(tenant.getMovein()));
		fieldTextData.put("moveout", Utils.getDate(tenant.getMoveout()));
		fieldTextData.put("occupants", tenant.getOccupants());
		fieldTextData.put("rent", unit.getRent()+" "+Utils.getMessage(messageSource, "lease.monthrent"));
		
		JSONObject fieldData=new JSONObject();
		fieldData.put(ZohoSignConstants.FIELD_TEXT_DATA, fieldTextData);
		
		JSONObject templatesJson = new JSONObject();
		templatesJson.put(ZohoSignConstants.ACTIONS, actions);
		templatesJson.put(ZohoSignConstants.FIELD_DATA, fieldData);
		
		JSONObject dataJson = new JSONObject();
		dataJson.put(ZohoSignConstants.TEMPLATES, templatesJson);
		
		MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
		reqEntity.addTextBody("data",dataJson.toString());
		reqEntity.addTextBody("is_quicksend", "true"); 
		HttpEntity multipart = reqEntity.build();
		
		HttpPost postMethod = new HttpPost(env.getProperty("zoho.sign.template"));
		postMethod.setHeader("Authorization","Zoho-oauthtoken "+ this.accessToken);
		postMethod.setEntity(multipart);
		
		HttpResponse response;
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
			response = httpClient.execute(postMethod);
			
			String responseJSON = EntityUtils.toString(response.getEntity(), "UTF-8");
			JSONObject submitResponse = new JSONObject(responseJSON);
			if(submitResponse.has("status") && submitResponse.getString("status").equals("success")){
				JSONObject responseObj=submitResponse.getJSONObject("requests");
				return responseObj.get("request_id").toString();    
			}else if(response.getStatusLine().getStatusCode()==401 && retry==0) {
				generateAccessToken();
				return createSignRequest(tenant, property, unit, 1);
			}
		} catch (IOException e) {
			throw new APIRequestException(Utils.getMessage(messageSource, "error.unkown_issue"));
		}
		
		return null;
	}
}
