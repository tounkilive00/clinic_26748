package com.mady.clinics.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mady.clinics.domain.Patient;

public interface PatientRepository extends JpaRepository<Patient, UUID> {
    
    // A1 - DERIVED: by last name, case insensitive, sorted by first name
    List<Patient> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);

    // Search by name (existing)
    List<Patient> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);

    // Search by date of birth (existing)
    List<Patient> findByDateOfBirth(java.sql.Date dateOfBirth);

    // B4 - JPQL: distinct patients of a doctor
    @Query("SELECT DISTINCT a.patient FROM Appointment a WHERE a.doctor.doctorId = :doctorId")
    List<Patient> findPatientsByDoctorId(@Param("doctorId") UUID doctorId);

    // C2 - JPQL: frequent patients (min appointments)
    @Query("SELECT a.patient FROM Appointment a GROUP BY a.patient HAVING COUNT(a) >= :min ORDER BY COUNT(a) DESC")
    List<Patient> findFrequentPatients(@Param("min") long min);

    // B4 - JPQL: patients with no appointments
    @Query("SELECT p FROM Patient p WHERE p.appointments IS EMPTY")
    List<Patient> findPatientsWithoutAppointments();

    boolean existsBySsNumber(String ssNumber);
    java.util.Optional<Patient> findBySsNumber(String ssNumber);
}
