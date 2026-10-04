package com.mady.clinics.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mady.clinics.domain.Patient;
import com.mady.clinics.repository.DoctorRepository;
import com.mady.clinics.repository.PatientRepository;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(UUID patientId) {
        Optional<Patient> patient = patientRepository.findById(patientId);
        if (patient.isPresent()) {
            return patient.get();
        } else {
            throw new RuntimeException("Patient not found with id: " + patientId);
        }
    }

    public void deletePatientById(UUID patientId) {
        patientRepository.deleteById(patientId);
    }

    public Patient updatePatient(UUID patientId, Patient updatedPatient) {
        Optional<Patient> existingPatient = patientRepository.findById(patientId);
        if (existingPatient.isPresent()) {
            Patient patient = existingPatient.get();
            patient.setFirstName(updatedPatient.getFirstName());
            patient.setLastName(updatedPatient.getLastName());
            patient.setDateOfBirth(updatedPatient.getDateOfBirth());
            return patientRepository.save(patient);
        } else {
            throw new RuntimeException("Patient not found with id: " + patientId);
        }
    }

    // Existing search by name
    public List<Patient> searchByName(String name) {
        return patientRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
    }

    // A1: find by last name (case-insensitive, sorted by first name)
    public List<Patient> findByLastName(String lastName) {
        return patientRepository.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }

    // B4: patients of a specific doctor
    public Optional<List<Patient>> getPatientsOfDoctor(UUID doctorId) {
        if (!doctorRepository.existsById(doctorId)) {
            return Optional.empty();
        }
        return Optional.of(patientRepository.findPatientsByDoctorId(doctorId));
    }

    // C2: frequent patients with at least min appointments
    public List<Patient> getFrequentPatients(long min) {
        return patientRepository.findFrequentPatients(min);
    }

    // B4: patients with no appointments
    public List<Patient> getPatientsWithoutAppointments() {
        return patientRepository.findPatientsWithoutAppointments();
    }
}
