package com.mady.clinics.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.mady.clinics.domain.Specialization;

public interface SpecializationsRepository extends JpaRepository<Specialization, UUID> {
    
    // B3 - JPQL: specializations with no doctors
    @Query("SELECT s FROM Specialization s WHERE s.doctors IS EMPTY")
    List<Specialization> findUnusedSpecializations();
}
