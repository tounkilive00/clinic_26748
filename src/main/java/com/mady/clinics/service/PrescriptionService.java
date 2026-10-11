package com.mady.clinics.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mady.clinics.domain.Appointment;
import com.mady.clinics.domain.AppointmentStatus;
import com.mady.clinics.domain.Prescription;
import com.mady.clinics.repository.AppointmentRepository;
import com.mady.clinics.repository.PrescriptionRepository;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    // C1: Write a prescription
    public String savePrescription(UUID appointmentId, Prescription prescription) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);
        if (appointmentOpt.isEmpty()) {
            return "The appointment with that id does not exist";
        }
        Appointment appointment = appointmentOpt.get();
        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            return "Prescriptions are only for COMPLETED appointments";
        }
        prescription.setIssuedDate(LocalDate.now());
        prescription.setAppointment(appointment);
        prescriptionRepository.save(prescription);
        return "Prescription saved successfully";
    }

    // C2: Prescriptions of an appointment (DERIVED)
    public Optional<List<Prescription>> getPrescriptionsByAppointment(UUID appointmentId) {
        if (!appointmentRepository.existsById(appointmentId)) {
            return Optional.empty();
        }
        return Optional.of(prescriptionRepository.findByAppointment_AppointmentId(appointmentId));
    }

    // C3: Prescription history of a patient (JPQL)
    public List<Prescription> getPrescriptionsByPatient(String ssNumber) {
        return prescriptionRepository.findByPatientSsNumber(ssNumber);
    }

    // C4: Update prescription
    public String updatePrescription(UUID id, Prescription updated) {
        Optional<Prescription> opt = prescriptionRepository.findById(id);
        if (opt.isEmpty()) {
            return "The prescription with that id does not exist";
        }
        Prescription existing = opt.get();
        existing.setMedicineName(updated.getMedicineName());
        existing.setDosage(updated.getDosage());
        existing.setDurationDays(updated.getDurationDays());
        prescriptionRepository.save(existing);
        return "Prescription updated successfully";
    }

    // C4: Delete prescription
    public String deletePrescription(UUID id) {
        if (!prescriptionRepository.existsById(id)) {
            return "The prescription with that id does not exist";
        }
        prescriptionRepository.deleteById(id);
        return "Prescription deleted successfully";
    }

    // Bonus +5: Top medicines
    public List<Object[]> getTopMedicines() {
        return prescriptionRepository.findTopMedicines();
    }
}
