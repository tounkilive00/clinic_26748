package com.mady.clinics.service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mady.clinics.domain.AppointmentStatus;
import com.mady.clinics.repository.AppointmentRepository;
import com.mady.clinics.repository.DoctorRepository;
import com.mady.clinics.repository.OfficeRepository;
import com.mady.clinics.repository.PatientRepository;
import com.mady.clinics.repository.PrescriptionRepository;

@Service
public class DashboardService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    public Map<String, Long> getDashboardSummary() {
        Map<String, Long> summary = new LinkedHashMap<>();
        summary.put("patients", patientRepository.count());
        summary.put("doctors", doctorRepository.count());
        summary.put("freeOffices", (long) officeRepository.findFreeOffices().size());
        summary.put("appointmentsToday", appointmentRepository.countByAppointmentDate(LocalDate.now()));
        summary.put("scheduled", appointmentRepository.countByStatus(AppointmentStatus.SCHEDULED));
        summary.put("prescriptions", prescriptionRepository.count());
        return summary;
    }
}
