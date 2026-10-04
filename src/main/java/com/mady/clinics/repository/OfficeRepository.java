package com.mady.clinics.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.mady.clinics.domain.Office;

public interface OfficeRepository extends JpaRepository<Office, UUID> {
    Optional<Office> findByOfficeName(String officeName);
    Optional<Office> findByOfficeNumber(int officeNumber);
}
