package com.mady.clinics.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mady.clinics.domain.Appointment;
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
}
