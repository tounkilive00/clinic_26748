package com.mady.clinics.domain;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity 

@Table (name = "offices")
public class Office {
    @Id 
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID officeId;
    @Column (name = "office_name")
    private String officeName;
    @Column (name = "office_number")
    private int officeNumber;


// generate getters and setters for the fields    
    public UUID getOfficeId() {
        return officeId;
    }
    public void setOfficeId(UUID officeId) {
        this.officeId = officeId;
    }
    public String getOfficeName() {
        return officeName;
    }
    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }
    public int getOfficeNumber() {
        return officeNumber;
    }
    public void setOfficeNumber(int officeNumber) {
        this.officeNumber = officeNumber;
    }
    public Doctor getDoctor() {
        return doctor;
    }   
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }


    // relationships with other entities can be added here, such as doctors, patients, etc.

    @JsonIgnore
    @OneToOne(mappedBy = "office") 
    private Doctor doctor;

}

