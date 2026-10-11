package com.mady.clinics.controller;

import java.util.List;
import java.util.Optional;
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

import com.mady.clinics.domain.Patient;
import com.mady.clinics.service.PatientService;

@RestController
@RequestMapping("/api/patients")
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
    public ResponseEntity<Patient> getPatientById(@PathVariable UUID id) {
        return new ResponseEntity<>(patientService.getPatientById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable UUID id, @RequestBody Patient patient) {
        Patient updated = patientService.updatePatient(id, patient);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable UUID id) {
        patientService.deletePatientById(id);
        return new ResponseEntity<>("Patient deleted successfully", HttpStatus.OK);
    }

    // Existing search
    @GetMapping("/search")
    public ResponseEntity<List<Patient>> searchPatients(@RequestParam(required = false) String name) {
        if (name != null && !name.isBlank()) {
            return new ResponseEntity<>(patientService.searchByName(name), HttpStatus.OK);
        }
        return new ResponseEntity<>(patientService.getAllPatients(), HttpStatus.OK);
    }

    // A1: by last name
    @GetMapping("/by-last-name")
    public ResponseEntity<List<Patient>> getByLastName(@RequestParam String lastName) {
        return new ResponseEntity<>(patientService.findByLastName(lastName), HttpStatus.OK);
    }

    // B4: patients of a specific doctor
    @GetMapping("/of-doctor/{doctorId}")
    public ResponseEntity<?> getPatientsOfDoctor(@PathVariable UUID doctorId) {
        Optional<List<Patient>> patients = patientService.getPatientsOfDoctor(doctorId);
        if (patients.isEmpty()) {
            return new ResponseEntity<>("The doctor with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(patients.get(), HttpStatus.OK);
    }

    // C2: frequent patients
    @GetMapping("/frequent")
    public ResponseEntity<List<Patient>> getFrequentPatients(@RequestParam(defaultValue = "3") long min) {
        return new ResponseEntity<>(patientService.getFrequentPatients(min), HttpStatus.OK);
    }

    // B4: GET /api/patients/no-appointments
    @GetMapping("/no-appointments")
    public ResponseEntity<List<Patient>> getPatientsWithoutAppointments() {
        return new ResponseEntity<>(patientService.getPatientsWithoutAppointments(), HttpStatus.OK);
    }

    // D2: Upcoming appointments of a patient (DERIVED)
    @GetMapping("/{ssNumber}/upcoming")
    public ResponseEntity<?> getUpcomingAppointments(@PathVariable String ssNumber) {
        Optional<List<com.mady.clinics.domain.Appointment>> list = patientService.getUpcomingAppointments(ssNumber);
        if (list.isEmpty()) {
            return new ResponseEntity<>("That patient with that ssnumber does not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list.get(), HttpStatus.OK);
    }
}
