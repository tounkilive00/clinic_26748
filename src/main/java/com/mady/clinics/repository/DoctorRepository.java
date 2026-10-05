package com.mady.clinics.repository;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mady.clinics.domain.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

    // Existing search methods
    List<Doctor> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
    List<Doctor> findByDateOfBirth(Date dateOfBirth);

    // B1 - JPQL: doctors by specialization name (case-insensitive join on collection)
    @Query("SELECT d FROM Doctor d JOIN d.specializations s WHERE LOWER(s.specializationName) = LOWER(:name)")
    List<Doctor> findBySpecializationName(@Param("name") String name);

    // B2 - JPQL: doctors without an office
    @Query("SELECT d FROM Doctor d WHERE d.office IS NULL ORDER BY d.lastName ASC")
    List<Doctor> findDoctorsWithoutOffice();

    java.util.Optional<Doctor> findByOfficeOfficeId(UUID officeId);
}
