package com.mady.clinics.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

import com.mady.clinics.domain.Appointment;
import com.mady.clinics.domain.AppointmentStatus;
import com.mady.clinics.service.AppointmentService;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    // CREATE with A4 double-booking check
    @PostMapping({"", "/save"})
    public ResponseEntity<?> saveAppointment(@RequestBody Appointment appointment) {
        if (appointment.getDoctor() != null && appointment.getDoctor().getDoctorId() != null
                && appointment.getAppointmentDate() != null) {
            if (appointmentService.isDoctorBooked(appointment.getDoctor().getDoctorId(), appointment.getAppointmentDate())) {
                return new ResponseEntity<>("Doctor is already booked on that date", HttpStatus.CONFLICT);
            }
        }
        Appointment saved = appointmentService.saveAppointment(appointment);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return new ResponseEntity<>(appointmentService.getAllAppointments(), HttpStatus.OK);
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable UUID id) {
        return new ResponseEntity<>(appointmentService.getAppointmentById(id), HttpStatus.OK);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable UUID id, @RequestBody Appointment appointment) {
        Appointment updated = appointmentService.updateAppointment(id, appointment);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable UUID id) {
        appointmentService.deleteAppointment(id);
        return new ResponseEntity<>("Appointment deleted successfully", HttpStatus.OK);
    }

    // A2: by status
    @GetMapping("/by-status")
    public ResponseEntity<List<Appointment>> getByStatus(@RequestParam AppointmentStatus status) {
        return new ResponseEntity<>(appointmentService.findByStatus(status), HttpStatus.OK);
    }

    // A3: between dates (parse strings to LocalDate in controller)
    @GetMapping("/between")
    public ResponseEntity<List<Appointment>> getBetween(@RequestParam String start, @RequestParam String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        return new ResponseEntity<>(appointmentService.findBetweenDates(startDate, endDate), HttpStatus.OK);
    }

    // C1: stats by status
    @GetMapping("/stats/by-status")
    public ResponseEntity<List<Object[]>> getStatsByStatus() {
        return new ResponseEntity<>(appointmentService.getAppointmentStatsByStatus(), HttpStatus.OK);
    }

    // C4: cancel all appointments for a doctor on a date
    @PatchMapping("/cancel-day")
    public ResponseEntity<String> cancelDay(@RequestParam UUID doctorId, @RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        return new ResponseEntity<>(appointmentService.cancelDayForDoctor(doctorId, localDate), HttpStatus.OK);
    }

    // Bonus +5: paginated appointments
    @GetMapping("/page")
    public ResponseEntity<Page<Appointment>> getPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "appointmentDate,desc") String sort) {
        String[] sortParts = sort.split(",");
        String field = sortParts[0];
        Sort.Direction dir = (sortParts.length > 1 && sortParts[1].equalsIgnoreCase("asc"))
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        return new ResponseEntity<>(
                appointmentService.getAppointmentsPage(PageRequest.of(page, size, Sort.by(dir, field))),
                HttpStatus.OK);
    }

    // Bonus +5: delete cancelled before date
    @DeleteMapping("/cancelled-before")
    public ResponseEntity<String> deleteCancelledBefore(@RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        return new ResponseEntity<>(appointmentService.deleteCancelledBefore(localDate), HttpStatus.OK);
    }
}
