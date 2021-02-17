package com.rentpal.agreement.model;

/**
 * @author frank
 * @created 19 Dec,2020 - 9:42 PM
 */

import javax.persistence.*;
import java.util.Date;

@Entity
//@ToString
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String dob;

    @Column(nullable = false)
    private String nationality;

    @ManyToOne
    @JoinColumn(name="unitId", referencedColumnName = "id", nullable = false)
    private Unit unit;

    @Column(nullable = false)
    private Date movein;

    @Column(nullable = false)
    private Date moveout;

    @Column(nullable = false)
    private Integer occupants;

    @ManyToOne
    @JoinColumn(name="userId", referencedColumnName = "id", nullable = false)
    private User user;

    public Date getMovein() {
        return movein;
    }

    public void setMovein(Date movein) {
        this.movein = movein;
    }

    public Date getMoveout() {
        return moveout;
    }

    public void setMoveout(Date moveout) {
        this.moveout = moveout;
    }

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

    public Unit getUnit() { return unit; }

    public void setUnit(Unit unit) { this.unit = unit; }

    public Integer getOccupants() {
        return occupants;
    }

    public void setOccupants(Integer occupants) {
        this.occupants = occupants;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
