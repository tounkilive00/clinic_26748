package com.mady.clinics.repository;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mady.clinics.domain.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

    List<Doctor> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);

    List<Doctor> findByDateOfBirth(Date dateOfBirth);
}

