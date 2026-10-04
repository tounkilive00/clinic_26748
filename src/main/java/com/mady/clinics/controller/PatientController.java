package com.mady.clinics.controller;

import java.sql.Date;
import java.util.List;

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
import com.mady.clinics.service.PatientService;
import com.mady.clinics.domain.Patient;


@RestController 
@RequestMapping ("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping("/save")
    public ResponseEntity<Patient> savePatient(@RequestBody Patient patient) {
        Patient saved = patientService.save(patient);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        return new ResponseEntity<>(patientService.getAllPatients(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable java.util.UUID id) {
        return new ResponseEntity<>(patientService.getPatientById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable java.util.UUID id, @RequestBody Patient patient) {
        Patient updated = patientService.updatePatient(id, patient);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable java.util.UUID id) {
        patientService.deletePatientById(id);
        return new ResponseEntity<>("Patient deleted successfully", HttpStatus.OK);
    }

    // Search patients by name or date of birth
    // GET /api/patients/search?name=John or GET /api/patients/search?dob=1990-08-20
    @GetMapping("/search")
    public ResponseEntity<List<Patient>> searchPatients(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Date dob) {
        if (name != null && !name.isBlank()) {
            return new ResponseEntity<>(patientService.searchByName(name), HttpStatus.OK);
        } else if (dob != null) {
            return new ResponseEntity<>(patientService.searchByDateOfBirth(dob), HttpStatus.OK);
        }
        return new ResponseEntity<>(patientService.getAllPatients(), HttpStatus.OK);
    }
}

