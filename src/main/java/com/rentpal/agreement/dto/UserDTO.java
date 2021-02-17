package com.rentpal.agreement.dto;

/**
 * @author frank
 * @created 10 Feb,2021 - 5:29 PM
 */


public class UserDTO {
    private Long id;

    private String email;

    private String creationTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }
}
