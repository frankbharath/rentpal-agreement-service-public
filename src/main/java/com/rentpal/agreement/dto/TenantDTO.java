package com.rentpal.agreement.dto;

/*
 * @author frank
 * @created 19 Dec,2020 - 11:29 PM
 */

public class TenantDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String dob;

    private String nationality;

    private Long unitId;

    private String movein;

    private String moveout;

    private Integer occupants;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getMovein() {
        return movein;
    }

    public void setMovein(String movein) {
        this.movein = movein;
    }

    public String getMoveout() {
        return moveout;
    }

    public void setMoveout(String moveout) {
        this.moveout = moveout;
    }

    public Integer getOccupants() {
        return occupants;
    }

    public void setOccupants(Integer occupants) {
        this.occupants = occupants;
    }
}
