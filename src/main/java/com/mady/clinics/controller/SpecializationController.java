package com.mady.clinics.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mady.clinics.domain.Specialization;
import com.mady.clinics.service.SpecializationService;

@RestController
@RequestMapping("/api/specializations")
public class SpecializationController {

    @Autowired
    private SpecializationService specializationService;

    @PostMapping({"", "/save"})
    public ResponseEntity<Specialization> saveSpecialization(@RequestBody Specialization specialization) {
        Specialization saved = specializationService.saveSpecialization(specialization);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Specialization>> getAllSpecializations() {
        return new ResponseEntity<>(specializationService.getAllSpecializations(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Specialization> getSpecializationById(@PathVariable UUID id) {
        return new ResponseEntity<>(specializationService.getSpecializationById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSpecialization(@PathVariable UUID id) {
        specializationService.deleteSpecialization(id);
        return new ResponseEntity<>("Specialization deleted successfully", HttpStatus.OK);
    }

    // B3: unused specializations
    @GetMapping("/unused")
    public ResponseEntity<List<Specialization>> getUnusedSpecializations() {
        return new ResponseEntity<>(specializationService.getUnusedSpecializations(), HttpStatus.OK);
    }
}
