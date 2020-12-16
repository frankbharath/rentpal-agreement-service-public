package com.rentpal.agreement.model;

import javax.persistence.*;

/**
 * Domain model for unit.
 * @author bharath
 * @version 1.0
 * Creation time: Aug 12, 2020 3:05:40 PM
 */

@Entity
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name="propertyId", referencedColumnName = "id", nullable = false)
    private Property property;

    @Column(nullable = false)
    private Integer bedrooms;

    @Column(nullable = false)
    private Integer bathrooms;

    @Column(nullable = false)
    private Float area;

    @Column(nullable = false)
    private Float rent;

    @Column(nullable = false)
    private Float cautionDeposit;

    @Column(nullable = false)
    private Boolean furnished;

    @Column(nullable = false, length = 8)
    private String doorNumber;

    @Column(nullable = false)
    private Integer floorNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public Integer getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(Integer bedrooms) {
        this.bedrooms = bedrooms;
    }

    public Integer getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(Integer bathrooms) {
        this.bathrooms = bathrooms;
    }

    public Float getArea() {
        return area;
    }

    public void setArea(Float area) {
        this.area = area;
    }

    public Float getRent() {
        return rent;
    }

    public void setRent(Float rent) {
        this.rent = rent;
    }

    public Float getCautionDeposit() {
        return cautionDeposit;
    }

    public void setCautionDeposit(Float cautionDeposit) {
        this.cautionDeposit = cautionDeposit;
    }

    public Boolean getFurnished() {
        return furnished;
    }

    public void setFurnished(Boolean furnished) {
        this.furnished = furnished;
    }

    public String getDoorNumber() {
        return doorNumber;
    }

    public void setDoorNumber(String doorNumber) {
        this.doorNumber = doorNumber;
    }

    public Integer getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(Integer floorNumber) {
        this.floorNumber = floorNumber;
    }

}
