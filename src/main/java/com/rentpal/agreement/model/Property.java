package com.rentpal.agreement.model;

import lombok.ToString;

import javax.persistence.*;
import java.util.List;

/**
 * Domain model for property.
 * @author bharath
 * @version 1.0
 * Creation time: Aug 11, 2020 2:50:39 PM
 */

@Entity
//@ToString
public class Property {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@ManyToOne
	@JoinColumn(name="userId", referencedColumnName = "id", nullable = false)
	private User user;

	@Column(nullable = false, length = 64)
	private String propertyName;

	@Column(nullable = false)
	private Long creationTime;

	@Column(nullable = false, length = 128)
	private String addressLine1;

	@Column(length = 128)
	private String addressLine2;

	@Column(nullable = false, length = 64)
	private String city;

	@Column(nullable = false, length = 16)
	private String postal;

	@OneToMany(fetch = FetchType.LAZY, mappedBy="property", cascade = CascadeType.REMOVE)
	private List<Unit> units;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public Long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Long creationTime) {
		this.creationTime = creationTime;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostal() {
		return postal;
	}

	public void setPostal(String postal) {
		this.postal = postal;
	}

	public List<Unit> getUnits() {
		return units;
	}

	public void setUnits(List<Unit> units) {
		this.units = units;
	}

}
