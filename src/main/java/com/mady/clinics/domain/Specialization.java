package com.mady.clinics.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "specializations")
public class Specialization {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID specializationId;

    @Column(name = "specialization_name")
    private String specializationName;

    @JsonIgnore
    @ManyToMany(mappedBy = "specializations")
    private List<Doctor> doctors = new ArrayList<>();

    // Getters and Setters
    public UUID getSpecializationId() { return specializationId; }
    public void setSpecializationId(UUID specializationId) { this.specializationId = specializationId; }

    public String getSpecializationName() { return specializationName; }
    public void setSpecializationName(String specializationName) { this.specializationName = specializationName; }

    public List<Doctor> getDoctors() { return doctors; }
    public void setDoctors(List<Doctor> doctors) { this.doctors = doctors; }

    // equals and hashCode based on specializationId so List.contains() and remove() work correctly
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Specialization that = (Specialization) o;
        return specializationId != null && specializationId.equals(that.specializationId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
