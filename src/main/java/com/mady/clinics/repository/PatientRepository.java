package com.mady.clinics.repository;

import java.sql.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.mady.clinics.domain.Patient;

public interface PatientRepository extends JpaRepository<Patient, UUID> {

    List<Patient> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);

    List<Patient> findByDateOfBirth(Date dateOfBirth);
}

