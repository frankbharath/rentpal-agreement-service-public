package com.rentpal.agreement.dto;

/*
 * @author frank
 * @created 15 Dec,2020 - 10:20 PM
 */

import com.rentpal.agreement.model.Property;

import javax.persistence.*;

public class UnitDTO {

    private Long id;

    private Long propertyId;

    private Integer bedrooms;

    private Integer bathrooms;

    private Float area;

    private Float rent;

    private Float cautionDeposit;

    private Boolean furnished;

    private String doorNumber;

    private Integer floorNumber;

    private PropertyDTO propertyDTO;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
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

    public PropertyDTO getPropertyDTO() { return propertyDTO; }

    public void setPropertyDTO(PropertyDTO propertyDTO) { this.propertyDTO = propertyDTO; }
}
