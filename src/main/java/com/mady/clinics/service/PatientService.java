package com.mady.clinics.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mady.clinics.repository.PatientRepository;
import com.mady.clinics.domain.Patient;
@Service 
public class PatientService {
    
    @Autowired 
    private PatientRepository patientRepository;
    
    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }

    public List<Patient> getAllPatients(){
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

    public void deletePatientById(java.util.UUID patientId){
        patientRepository.deleteById(patientId);
    }

    public Patient updatePatient(UUID patientId , Patient updatedPatient){
        Optional<Patient> existingPatient = patientRepository.findById(patientId);
        if(existingPatient.isPresent()){
            Patient patient = existingPatient.get();
            patient.setFirstName(updatedPatient.getFirstName());
            patient.setLastName(updatedPatient.getLastName());
            patient.setDateOfBirth(updatedPatient.getDateOfBirth());
            return patientRepository.save(patient);
        }
        else{
            throw new RuntimeException("Patient not found with id: " + patientId);
        }
    }

    // Search patients by first or last name
    public List<Patient> searchByName(String name) {
        return patientRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
    }

    // Search patients by date of birth
    public List<Patient> searchByDateOfBirth(java.sql.Date dateOfBirth) {
        return patientRepository.findByDateOfBirth(dateOfBirth);
    }
}

