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

import com.mady.clinics.domain.Prescription;
import com.mady.clinics.service.PrescriptionService;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    // C1: Write a prescription
    @PostMapping("/save")
    public ResponseEntity<String> savePrescription(
            @RequestParam UUID appointmentId,
            @RequestBody Prescription prescription) {
        String res = prescriptionService.savePrescription(appointmentId, prescription);
        if (res.equals("Prescription saved successfully")) {
            return new ResponseEntity<>(res, HttpStatus.CREATED);
        } else if (res.equals("The appointment with that id does not exist")) {
            return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
    }

    // C2: Prescriptions of an appointment
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<?> getPrescriptionsByAppointment(@PathVariable UUID appointmentId) {
        Optional<List<Prescription>> list = prescriptionService.getPrescriptionsByAppointment(appointmentId);
        if (list.isEmpty()) {
            return new ResponseEntity<>("The appointment with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list.get(), HttpStatus.OK);
    }

    // C3: Prescription history of a patient
    @GetMapping("/patient/{ssNumber}")
    public ResponseEntity<List<Prescription>> getPrescriptionsByPatient(@PathVariable String ssNumber) {
        return new ResponseEntity<>(prescriptionService.getPrescriptionsByPatient(ssNumber), HttpStatus.OK);
    }

    // C4: Update prescription
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updatePrescription(@PathVariable UUID id, @RequestBody Prescription prescription) {
        String res = prescriptionService.updatePrescription(id, prescription);
        if (res.equals("Prescription updated successfully")) {
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
    }

    // C4: Delete prescription
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePrescription(@PathVariable UUID id) {
        String res = prescriptionService.deletePrescription(id);
        if (res.equals("Prescription deleted successfully")) {
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
    }

    // Bonus +5: Top medicines
    @GetMapping("/stats/top-medicines")
    public ResponseEntity<List<Object[]>> getTopMedicines() {
        return new ResponseEntity<>(prescriptionService.getTopMedicines(), HttpStatus.OK);
    }
}
