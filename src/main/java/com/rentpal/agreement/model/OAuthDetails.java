package com.rentpal.agreement.model;

/**
 * The Class OAuthDetails.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Sep 28, 2020 11:15:08 PM
 */
public class OAuthDetails {

	/** The client id. */
	public String clientId;
	
	/** The client secret. */
	public String clientSecret;
	
	/** The refresh token. */
	public String refreshToken;
	
	/** The access token. */
	public String accessToken;
	
	/**
	 * Gets the client id.
	 *
	 * @return the client id
	 */
	public String getClientId() {
		return clientId;
	}
	
	/**
	 * Sets the client id.
	 *
	 * @param clientId the new client id
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	/**
	 * Gets the client secret.
	 *
	 * @return the client secret
	 */
	public String getClientSecret() {
		return clientSecret;
	}
	
	/**
	 * Sets the client secret.
	 *
	 * @param clientSecret the new client secret
	 */
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	
	/**
	 * Gets the refresh token.
	 *
	 * @return the refresh token
	 */
	public String getRefreshToken() {
		return refreshToken;
	}
	
	/**
	 * Sets the refresh token.
	 *
	 * @param refreshToken the new refresh token
	 */
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
	/**
	 * Gets the access token.
	 *
	 * @return the access token
	 */
	public String getAccessToken() {
		return accessToken;
	}
	
	/**
	 * Sets the access token.
	 *
	 * @param accessToken the new access token
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}
