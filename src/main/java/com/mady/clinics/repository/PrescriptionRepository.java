package com.mady.clinics.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mady.clinics.domain.Prescription;

public interface PrescriptionRepository extends JpaRepository<Prescription, UUID> {

    // C2 - DERIVED: find prescriptions of an appointment
    List<Prescription> findByAppointment_AppointmentId(UUID appointmentId);

    // C3 - JPQL: path navigation p.appointment.patient.ssNumber
    @Query("SELECT p FROM Prescription p WHERE p.appointment.patient.ssNumber = :ssNumber ORDER BY p.issuedDate DESC")
    List<Prescription> findByPatientSsNumber(@Param("ssNumber") String ssNumber);

    // C4 - DERIVED: check if appointment has prescriptions
    boolean existsByAppointment_AppointmentId(UUID appointmentId);

    // Bonus +5 - JPQL: top prescribed medicines
    @Query("SELECT p.medicineName, COUNT(p) FROM Prescription p GROUP BY p.medicineName ORDER BY COUNT(p) DESC")
    List<Object[]> findTopMedicines();
}
