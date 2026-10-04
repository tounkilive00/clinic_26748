package com.mady.clinics.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mady.clinics.domain.Appointment;
import com.mady.clinics.domain.AppointmentStatus;
import com.mady.clinics.domain.Doctor;
import com.mady.clinics.domain.Patient;
import com.mady.clinics.repository.AppointmentRepository;
import com.mady.clinics.repository.DoctorRepository;
import com.mady.clinics.repository.PatientRepository;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    // Save with double-booking check (A4)
    public Appointment saveAppointment(Appointment appointment) {
        if (appointment.getDoctor() != null && appointment.getDoctor().getDoctorId() != null) {
            UUID doctorId = appointment.getDoctor().getDoctorId();
            Doctor doctor = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + doctorId));
            appointment.setDoctor(doctor);
        }
        if (appointment.getPatient() != null && appointment.getPatient().getPatientId() != null) {
            UUID patientId = appointment.getPatient().getPatientId();
            Patient patient = patientRepository.findById(patientId)
                    .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));
            appointment.setPatient(patient);
        }
        return appointmentRepository.save(appointment);
    }

    // A4: check if doctor is already booked on date (not CANCELLED)
    public boolean isDoctorBooked(UUID doctorId, LocalDate date) {
        return appointmentRepository.existsByDoctor_DoctorIdAndAppointmentDateAndStatusNot(
                doctorId, date, AppointmentStatus.CANCELLED);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointment getAppointmentById(UUID appointmentId) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if (appointment.isPresent()) {
            return appointment.get();
        } else {
            throw new RuntimeException("Appointment not found with id: " + appointmentId);
        }
    }

    public Appointment updateAppointment(UUID appointmentId, Appointment updatedAppointment) {
        Optional<Appointment> existingAppointment = appointmentRepository.findById(appointmentId);
        if (existingAppointment.isPresent()) {
            Appointment appointment = existingAppointment.get();
            appointment.setAppointmentDate(updatedAppointment.getAppointmentDate());
            appointment.setReason(updatedAppointment.getReason());
            appointment.setStatus(updatedAppointment.getStatus());
            if (updatedAppointment.getDoctor() != null && updatedAppointment.getDoctor().getDoctorId() != null) {
                UUID doctorId = updatedAppointment.getDoctor().getDoctorId();
                Doctor doctor = doctorRepository.findById(doctorId)
                        .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + doctorId));
                appointment.setDoctor(doctor);
            }
            if (updatedAppointment.getPatient() != null && updatedAppointment.getPatient().getPatientId() != null) {
                UUID patientId = updatedAppointment.getPatient().getPatientId();
                Patient patient = patientRepository.findById(patientId)
                        .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));
                appointment.setPatient(patient);
            }
            return appointmentRepository.save(appointment);
        } else {
            throw new RuntimeException("Appointment not found with id: " + appointmentId);
        }
    }

    public void deleteAppointment(UUID appointmentId) {
        appointmentRepository.deleteById(appointmentId);
    }

    // A2: by status ordered by date
    public List<Appointment> findByStatus(AppointmentStatus status) {
        return appointmentRepository.findByStatusOrderByAppointmentDateAsc(status);
    }

    // A3: between two dates
    public List<Appointment> findBetweenDates(LocalDate start, LocalDate end) {
        return appointmentRepository.findByAppointmentDateBetweenOrderByAppointmentDateAsc(start, end);
    }

    // C1: count per status
    public List<Object[]> getAppointmentStatsByStatus() {
        return appointmentRepository.countAppointmentsByStatus();
    }

    // C3: busiest office
    public Optional<Object[]> getBusiestOffice() {
        List<Object[]> results = appointmentRepository.findBusiestOffice(PageRequest.of(0, 1));
        if (results.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(results.get(0));
    }

    // C4: cancel whole day for doctor (exclude COMPLETED)
    @Transactional
    public String cancelDayForDoctor(UUID doctorId, LocalDate date) {
        int count = appointmentRepository.cancelAppointmentsForDoctorOnDate(doctorId, date);
        return count + " appointments cancelled";
    }

    // Bonus +5: paginated appointments
    public Page<Appointment> getAppointmentsPage(Pageable pageable) {
        return appointmentRepository.findAll(pageable);
    }

    // Bonus +5: delete cancelled before date
    @Transactional
    public String deleteCancelledBefore(LocalDate date) {
        int count = appointmentRepository.deleteCancelledBefore(date);
        return count + " appointments deleted";
    }
}
