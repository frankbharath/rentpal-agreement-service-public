package com.rentpal.agreement.dto;


/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 18, 2020 2:17:15 AM
*/

public class PropertyDTO {
	
	/** The propertyid. */
	private Long id;

	/** The user */
	private Long userId;
	
	/** The name. */
	private String propertyname;
	
	/** The creationtime. */
	private String creationtime;
	
	/** The line 1. */
	private String addressline_1;
	
	/** The line 2. */
	private String addressline_2;
	
	/** The city. */
	private String city;
	
	/** The postal. */
	private String postal;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return the propertyname
	 */
	public String getPropertyname() {
		return propertyname;
	}

	/**
	 * @param propertyname the propertyname to set
	 */
	public void setPropertyname(String propertyname) {
		this.propertyname = propertyname;
	}

	/**
	 * Gets the creationtime.
	 *
	 * @return the creationtime
	 */
	public String getCreationtime() {
		return creationtime;
	}
	
	/**
	 * Sets the creationtime.
	 *
	 * @param creationtime the new creationtime
	 */
	public void setCreationtime(String creationtime) {
		this.creationtime = creationtime;
	}

	/**
	 * @return the addressline_1
	 */
	public String getAddressline_1() {
		return addressline_1;
	}

	/**
	 * @param addressline_1 the addressline_1 to set
	 */
	public void setAddressline_1(String addressline_1) {
		this.addressline_1 = addressline_1;
	}

	/**
	 * @return the addressline_2
	 */
	public String getAddressline_2() {
		return addressline_2;
	}

	/**
	 * @param addressline_2 the addressline_2 to set
	 */
	public void setAddressline_2(String addressline_2) {
		this.addressline_2 = addressline_2;
	}

	/**
	 * Gets the city.
	 *
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Sets the city.
	 *
	 * @param city the new city
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the postal
	 */
	public String getPostal() {
		return postal;
	}

	/**
	 * @param postal the postal to set
	 */
	public void setPostal(String postal) {
		this.postal = postal;
	}

}
