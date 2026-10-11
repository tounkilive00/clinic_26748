package com.mady.clinics.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mady.clinics.domain.Office;

public interface OfficeRepository extends JpaRepository<Office, UUID> {
    Optional<Office> findByOfficeName(String officeName);
    Optional<Office> findByOfficeNumber(int officeNumber);

    @Query("SELECT o FROM Office o WHERE o.doctor IS NULL")
    List<Office> findVacantOffices();

    @Query("SELECT o FROM Office o WHERE NOT EXISTS (SELECT d FROM Doctor d WHERE d.office = o) ORDER BY o.officeNumber ASC")
    List<Office> findFreeOffices();
    
    @Query("SELECT a.doctor.office.officeName, a.doctor.office.officeNumber, COUNT(a) " +
           "FROM Appointment a WHERE a.doctor.office IS NOT NULL " +
           "GROUP BY a.doctor.office.officeName, a.doctor.office.officeNumber " +
           "ORDER BY COUNT(a) DESC")
    List<Object[]> findBusiestOffice(org.springframework.data.domain.Pageable pageable);
}
