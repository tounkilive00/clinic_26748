package com.mady.clinics.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mady.clinics.domain.Specialization;

public interface SpecializationsRepository extends JpaRepository<Specialization, UUID> {
    
}
