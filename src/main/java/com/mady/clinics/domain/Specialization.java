package com.mady.clinics.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity 

@Table (name = "specializations")
public class Specialization {
    @Id 
    @GeneratedValue(strategy = GenerationType.UUID )
    private UUID specializationId;
    @Column (name = "specialization_name")
    private String specializationName;

    // generate getters and setters for the fields
    public UUID getSpecializationId() {
        return specializationId;
    }
    public void setSpecializationId(UUID specializationId) {
        this.specializationId = specializationId;
    }
    public String getSpecializationName() {
        return specializationName;
    }
    public void setSpecializationName(String specializationName) {
        this.specializationName = specializationName;
    }

    // generate getters and setters for the doctors list
    public List<Doctor> getDoctors() {
        return doctors;
    }
    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }

    // relationships with other entities can be added here, such as doctors, patients, etc.
    @JsonIgnore
    @ManyToMany(mappedBy = "specializations")
    private List<Doctor> doctors = new ArrayList<>();
}

