package com.rentpal.agreement.model;

import org.springframework.http.HttpStatus;

/**
 * The Class APIRequestResponse.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Sep 14, 2020 7:27:39 PM
 * Class Description
 */
public class APIRequestResponse {

	/** The data. */
	Object data;

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * Sets the data.
	 *
	 * @param data the new data
	 */
	public void setData(Object data) {
		this.data = data;
	}

}
