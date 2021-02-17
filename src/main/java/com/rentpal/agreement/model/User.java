package com.rentpal.agreement.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * Domain model for user.
 * @author bharath
 * @version 1.0
 * Creation time: Jul 9, 2020 9:58:03 PM
 */

@Entity(name = "users")
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(length = 256, nullable = false)
	private String email;

	@Column(nullable = false)
	private Long creationTime;

	public Long getId() { return id; }

	public void setId(Long id) { this.id = id; }

	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public Long getCreationTime() { return creationTime; }

	public void setCreationTime(Long creationTime) { this.creationTime = creationTime; }

	@OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.PERSIST)
	private List<Property> properties;

	@OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.PERSIST)
	private List<Tenant> tenants;
}
