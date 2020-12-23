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
@Data
public class User {

	@Id
	private Long id;

	@Column(length = 256, nullable = false)
	private String email;

	public Long getId() { return id; }

	public void setId(Long id) { this.id = id; }

	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	@OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.PERSIST)
	private List<Property> properties;
}
