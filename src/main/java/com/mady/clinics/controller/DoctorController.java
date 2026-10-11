package com.mady.clinics.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.mady.clinics.domain.Appointment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    @PutMapping("/{doctorId}/specializations/{specializationId}")
public ResponseEntity<String> addSpecialization(@PathVariable UUID doctorId, @PathVariable UUID specializationId) {
    String res = DoctorService.addSpecialization(doctorId, specializationId);
    if (res.equals("Specialization added to the doctor")) {
        return new ResponseEntity<>(res, HttpStatus.OK);
    } else if (res.contains("does not exist")) {
        return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
    } else {
        return new ResponseEntity<>(res, HttpStatus.CONFLICT);
    }
}
    @PatchMapping("/{doctorId}/office")
public ResponseEntity<String> moveDoctorOffice(@PathVariable UUID doctorId, @RequestParam int officeNumber) {
    String res = DoctorService.moveDoctorOffice(doctorId, officeNumber);
    if (res.equals("Office changed successfully")) {
        return new ResponseEntity<>(res, HttpStatus.OK);
    } else if (res.contains("does not exist")) {
        return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
    } else {
        return new ResponseEntity<>(res, HttpStatus.CONFLICT);
    }
}

    // Remove specialization
    @DeleteMapping("/{doctorId}/specializations/{specializationId}")
public ResponseEntity<String> removeSpecialization(@PathVariable UUID doctorId, @PathVariable UUID specializationId) {
    String res = DoctorService.removeSpecialization(doctorId, specializationId);
    if (res.equals("Specialization removed from the doctor")) {
        return new ResponseEntity<>(res, HttpStatus.OK);
    } else if (res.contains("does not exist")) {
        return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
    } else {
        return new ResponseEntity<>(res, HttpStatus.CONFLICT);
    }
}

    // D3: search doctors by name (JPQL)
    @GetMapping("/search")
    public ResponseEntity<List<Doctor>> searchDoctors(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Date dob) {
        if (name != null && !name.isBlank()) {
            return new ResponseEntity<>(DoctorService.searchDoctorsByNameJPQL(name), HttpStatus.OK);
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

    // D1: Doctor's agenda for a day (DERIVED)
    @GetMapping("/{doctorId}/agenda")
    public ResponseEntity<?> getDoctorAgenda(@PathVariable UUID doctorId, @RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        Optional<List<Appointment>> agenda = DoctorService.getDoctorAgenda(doctorId, localDate);
        if (agenda.isEmpty()) {
            return new ResponseEntity<>("The doctor with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(agenda.get(), HttpStatus.OK);
    }

    // Bonus +5: Doctor workload (JPQL)
    @GetMapping("/workload")
    public ResponseEntity<List<Object[]>> getDoctorWorkload(@RequestParam String start, @RequestParam String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        return new ResponseEntity<>(DoctorService.getDoctorWorkload(startDate, endDate), HttpStatus.OK);
    }
}
