package com.mady.clinics.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mady.clinics.domain.Doctor;
import com.mady.clinics.domain.Office;
import com.mady.clinics.domain.Specialization;
import com.mady.clinics.repository.DoctorRepository;
import com.mady.clinics.repository.OfficeRepository;
import com.mady.clinics.repository.SpecializationsRepository;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private SpecializationsRepository specializationsRepository;

    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(UUID doctorId) {
        Optional<Doctor> doctor = doctorRepository.findById(doctorId);
        if (doctor.isPresent()) {
            return doctor.get();
        } else {
            throw new RuntimeException("Doctor not found with id: " + doctorId);
        }
    }

    public void deleteDoctor(UUID doctorId) {
        doctorRepository.deleteById(doctorId);
    }

    public Doctor updateDoctor(UUID doctorId, Doctor updatedDoctor) {
        Optional<Doctor> existingDoctor = doctorRepository.findById(doctorId);
        if (existingDoctor.isPresent()) {
            Doctor doctor = existingDoctor.get();
            doctor.setFirstName(updatedDoctor.getFirstName());
            doctor.setLastName(updatedDoctor.getLastName());
            doctor.setDateOfBirth(updatedDoctor.getDateOfBirth());

            // Update specializations if provided
            if (updatedDoctor.getSpecializations() != null) {
                List<Specialization> resolvedSpecs = new ArrayList<>();
                for (Specialization spec : updatedDoctor.getSpecializations()) {
                    if (spec.getSpecializationId() != null) {
                        specializationsRepository.findById(spec.getSpecializationId())
                                .ifPresent(resolvedSpecs::add);
                    }
                }
                doctor.setSpecializations(resolvedSpecs);
            }

            // Update office if provided
            if (updatedDoctor.getOffice() != null && updatedDoctor.getOffice().getOfficeId() != null) {
                officeRepository.findById(updatedDoctor.getOffice().getOfficeId())
                        .ifPresent(doctor::setOffice);
            }

            return doctorRepository.save(doctor);
        } else {
            throw new RuntimeException("Doctor not found with id: " + doctorId);
        }
    }

    // Assign office to doctor (safely handles re-assigning if office is taken)
    @Transactional
    public Doctor assignOffice(UUID doctorId, UUID officeId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + doctorId));
        Office office = officeRepository.findById(officeId)
                .orElseThrow(() -> new RuntimeException("Office not found with id: " + officeId));
        
        // If this office is currently held by another doctor, clear it from that doctor first and flush
        doctorRepository.findByOfficeOfficeId(officeId).ifPresent(previousDoctor -> {
            if (!previousDoctor.getDoctorId().equals(doctorId)) {
                previousDoctor.setOffice(null);
                doctorRepository.saveAndFlush(previousDoctor);
            }
        });

        doctor.setOffice(office);
        return doctorRepository.saveAndFlush(doctor);
    }

    // Existing search methods
    public List<Doctor> searchByName(String name) {
        return doctorRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
    }

    public List<Doctor> searchByDateOfBirth(Date dateOfBirth) {
        return doctorRepository.findByDateOfBirth(dateOfBirth);
    }

    // Add specialization (fixed with @Transactional so Hibernate flushes)
    @Transactional
    public Doctor addSpecializationToDoctor(UUID doctorId, UUID specializationId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + doctorId));
        Specialization spec = specializationsRepository.findById(specializationId)
                .orElseThrow(() -> new RuntimeException("Specialization not found with id: " + specializationId));
        if (!doctor.getSpecializations().contains(spec)) {
            doctor.getSpecializations().add(spec);
        }
        return doctorRepository.save(doctor);
    }

    // Remove specialization (fixed with @Transactional)
    @Transactional
    public Doctor removeSpecializationFromDoctor(UUID doctorId, UUID specializationId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + doctorId));
        Specialization spec = specializationsRepository.findById(specializationId)
                .orElseThrow(() -> new RuntimeException("Specialization not found with id: " + specializationId));
        doctor.getSpecializations().remove(spec);
        return doctorRepository.save(doctor);
    }

    // B1: doctors by specialization name
    public List<Doctor> getDoctorsBySpecialization(String name) {
        return doctorRepository.findBySpecializationName(name);
    }

    // B2: doctors without an office
    public List<Doctor> getDoctorsWithoutOffice() {
        return doctorRepository.findDoctorsWithoutOffice();
    }
}
