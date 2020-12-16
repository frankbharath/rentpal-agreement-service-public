package com.rentpal.agreement.model;

import org.springframework.http.HttpStatus;

/**
 * @author bharath
 * @version 1.0
 * Creation time: Sep 14, 2020 6:06:52 PM
 * This class error response object on API exception
*/

public class APIException {
	
	/** The timestamp. */
	String timestamp;
	
	/** The message. */
	Object message;
	
	/** The status. */
	String status;

	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public String getTimestamp() {
		return timestamp;
	}
	
	/**
	 * Sets the date.
	 *
	 * @param date the new date
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public Object getMessage() {
		return message;
	}
	
	/**
	 * Sets the message.
	 *
	 * @param message the new message
	 */
	public void setMessage(Object message) {
		this.message = message;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

}
