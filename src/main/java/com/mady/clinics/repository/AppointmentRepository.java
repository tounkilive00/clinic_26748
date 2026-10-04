package com.mady.clinics.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mady.clinics.domain.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    
}
