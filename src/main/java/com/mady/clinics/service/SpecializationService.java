package com.mady.clinics.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mady.clinics.domain.Specialization;
import com.mady.clinics.repository.SpecializationsRepository;

@Service
public class SpecializationService {

    @Autowired
    private SpecializationsRepository specializationsRepository;

    public Specialization saveSpecialization(Specialization specialization) {
        return specializationsRepository.save(specialization);
    }

    public List<Specialization> getAllSpecializations() {
        return specializationsRepository.findAll();
    }

    public Specialization getSpecializationById(UUID id) {
        Optional<Specialization> spec = specializationsRepository.findById(id);
        if (spec.isPresent()) {
            return spec.get();
        } else {
            throw new RuntimeException("Specialization not found with id: " + id);
        }
    }

    public void deleteSpecialization(UUID id) {
        specializationsRepository.deleteById(id);
    }

    public List<Specialization> getUnusedSpecializations() {
        return specializationsRepository.findUnusedSpecializations();
    }
}
