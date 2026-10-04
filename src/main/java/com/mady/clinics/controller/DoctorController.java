package com.mady.clinics.controller;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mady.clinics.domain.Doctor;
import com.mady.clinics.service.DoctorService;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService DoctorService;

    @PostMapping("/save")
    public ResponseEntity<Doctor> saveDoctor(@RequestBody Doctor doctor) {
        Doctor saved = DoctorService.saveDoctor(doctor);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        return new ResponseEntity<>(DoctorService.getAllDoctors(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable UUID id) {
        return new ResponseEntity<>(DoctorService.getDoctorById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable UUID id, @RequestBody Doctor doctor) {
        Doctor updated = DoctorService.updateDoctor(id, doctor);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoctor(@PathVariable UUID id) {
        DoctorService.deleteDoctor(id);
        return new ResponseEntity<>("Doctor deleted successfully", HttpStatus.OK);
    }

    // Assign office (supports both /office/ and /offices/)
    @PutMapping({"/{doctorId}/office/{officeId}", "/{doctorId}/offices/{officeId}"})
    public ResponseEntity<Doctor> assignOffice(@PathVariable UUID doctorId, @PathVariable UUID officeId) {
        Doctor updated = DoctorService.assignOffice(doctorId, officeId);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // Assign specialization (supports both /specialization/ and /specializations/)
    @PutMapping({"/{doctorId}/specialization/{specializationId}", "/{doctorId}/specializations/{specializationId}"})
    public ResponseEntity<Doctor> addSpecialization(@PathVariable UUID doctorId, @PathVariable UUID specializationId) {
        Doctor updated = DoctorService.addSpecializationToDoctor(doctorId, specializationId);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // Remove specialization
    @DeleteMapping({"/{doctorId}/specialization/{specializationId}", "/{doctorId}/specializations/{specializationId}"})
    public ResponseEntity<Doctor> removeSpecialization(@PathVariable UUID doctorId, @PathVariable UUID specializationId) {
        Doctor updated = DoctorService.removeSpecializationFromDoctor(doctorId, specializationId);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // Existing search
    @GetMapping("/search")
    public ResponseEntity<List<Doctor>> searchDoctors(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Date dob) {
        if (name != null && !name.isBlank()) {
            return new ResponseEntity<>(DoctorService.searchByName(name), HttpStatus.OK);
        } else if (dob != null) {
            return new ResponseEntity<>(DoctorService.searchByDateOfBirth(dob), HttpStatus.OK);
        }
        return new ResponseEntity<>(DoctorService.getAllDoctors(), HttpStatus.OK);
    }

    // B1: doctors by specialization
    @GetMapping("/by-specialization")
    public ResponseEntity<List<Doctor>> getBySpecialization(@RequestParam String name) {
        return new ResponseEntity<>(DoctorService.getDoctorsBySpecialization(name), HttpStatus.OK);
    }

    // B2: doctors without office
    @GetMapping("/without-office")
    public ResponseEntity<List<Doctor>> getDoctorsWithoutOffice() {
        return new ResponseEntity<>(DoctorService.getDoctorsWithoutOffice(), HttpStatus.OK);
    }
}
