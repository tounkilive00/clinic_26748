package com.mady.clinics.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mady.clinics.domain.Doctor;
import com.mady.clinics.domain.Office;
import com.mady.clinics.repository.DoctorRepository;
import com.mady.clinics.repository.OfficeRepository;

@Service
public class DoctorService {
    
    @Autowired 
    private DoctorRepository doctorRepository;

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private com.mady.clinics.repository.SpecializationsRepository specializationsRepository;


    // Add a method to save a doctor
    public Doctor saveDoctor(Doctor doctor){
        return doctorRepository.save(doctor);
    }
    
    public List<Doctor> getAllDoctors() { // Add a method to get all doctors
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(UUID doctorId) { // Add a method to get a doctor by ID
        Optional<Doctor> doctor = doctorRepository.findById(doctorId);
        if (doctor.isPresent()) {
            return doctor.get();
        } else {
            throw new RuntimeException("Doctor not found with id: " + doctorId);
        }
    }

    public void deleteDoctor(UUID doctorId) { // Add a method to delete a doctor by ID
        doctorRepository.deleteById(doctorId);
    }

    public Doctor updateDoctor(UUID doctorId, Doctor updatedDoctor) {
        Optional<Doctor> existingDoctor = doctorRepository.findById(doctorId);
        if (existingDoctor.isPresent()) {
            Doctor doctor = existingDoctor.get();
            doctor.setFirstName(updatedDoctor.getFirstName());
            doctor.setLastName(updatedDoctor.getLastName());
            doctor.setDateOfBirth(updatedDoctor.getDateOfBirth());

            // Update specializations if provided in request body
            if (updatedDoctor.getSpecializations() != null) {
                List<com.mady.clinics.domain.Specialization> resolvedSpecs = new ArrayList<>();
                for (com.mady.clinics.domain.Specialization spec : updatedDoctor.getSpecializations()) {
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


    // Assign an existing office to a doctor
    public Doctor assignOffice(UUID doctorId, UUID officeId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + doctorId));
        Office office = officeRepository.findById(officeId)
                .orElseThrow(() -> new RuntimeException("Office not found with id: " + officeId));
        doctor.setOffice(office);
        return doctorRepository.save(doctor);
    }

    // Search doctors by first or last name
    public List<Doctor> searchByName(String name) {

        return doctorRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
    }

    // Search doctors by date of birth
    public List<Doctor> searchByDateOfBirth(Date dateOfBirth) {
        return doctorRepository.findByDateOfBirth(dateOfBirth);
    }

    // Add a specialization to a doctor
    public Doctor addSpecializationToDoctor(UUID doctorId, UUID specializationId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + doctorId));
        com.mady.clinics.domain.Specialization spec = specializationsRepository.findById(specializationId)
                .orElseThrow(() -> new RuntimeException("Specialization not found with id: " + specializationId));
        if (!doctor.getSpecializations().contains(spec)) {
            doctor.getSpecializations().add(spec);
        }
        return doctorRepository.save(doctor);
    }

    // Remove a specialization from a doctor
    public Doctor removeSpecializationFromDoctor(UUID doctorId, UUID specializationId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + doctorId));
        com.mady.clinics.domain.Specialization spec = specializationsRepository.findById(specializationId)
                .orElseThrow(() -> new RuntimeException("Specialization not found with id: " + specializationId));
        doctor.getSpecializations().remove(spec);
        return doctorRepository.save(doctor);
    }
}



