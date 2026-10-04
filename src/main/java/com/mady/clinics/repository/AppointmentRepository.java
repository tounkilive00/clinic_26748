package com.mady.clinics.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mady.clinics.domain.Appointment;
import com.mady.clinics.domain.AppointmentStatus;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    // A2 - DERIVED: by status, ordered by date
    List<Appointment> findByStatusOrderByAppointmentDateAsc(AppointmentStatus status);

    // A3 - DERIVED: between two dates, ordered
    List<Appointment> findByAppointmentDateBetweenOrderByAppointmentDateAsc(LocalDate start, LocalDate end);

    // A4 - DERIVED: double booking check (status NOT CANCELLED)
    boolean existsByDoctor_DoctorIdAndAppointmentDateAndStatusNot(UUID doctorId, LocalDate date, AppointmentStatus status);

    // C1 - JPQL: count per status
    @Query("SELECT a.status, COUNT(a) FROM Appointment a GROUP BY a.status")
    List<Object[]> countAppointmentsByStatus();

    // C3 - JPQL: busiest office (pageable to get top 1)
    @Query("SELECT a.doctor.office.officeName, a.doctor.office.officeNumber, COUNT(a) " +
           "FROM Appointment a WHERE a.doctor.office IS NOT NULL " +
           "GROUP BY a.doctor.office.officeName, a.doctor.office.officeNumber " +
           "ORDER BY COUNT(a) DESC")
    List<Object[]> findBusiestOffice(Pageable pageable);

    // C4 - JPQL bulk UPDATE: cancel day for doctor (exclude COMPLETED)
    @Modifying
    @Query("UPDATE Appointment a SET a.status = com.mady.clinics.domain.AppointmentStatus.CANCELLED " +
           "WHERE a.doctor.doctorId = :doctorId AND a.appointmentDate = :date " +
           "AND a.status <> com.mady.clinics.domain.AppointmentStatus.COMPLETED")
    int cancelAppointmentsForDoctorOnDate(@Param("doctorId") UUID doctorId, @Param("date") LocalDate date);

    // Bonus +5: pageable
    Page<Appointment> findAll(Pageable pageable);

    // Bonus +5: delete cancelled before date
    @Modifying
    @Query("DELETE FROM Appointment a WHERE a.status = com.mady.clinics.domain.AppointmentStatus.CANCELLED AND a.appointmentDate < :date")
    int deleteCancelledBefore(@Param("date") LocalDate date);
}
