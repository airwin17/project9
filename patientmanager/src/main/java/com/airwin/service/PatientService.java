package com.airwin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.airwin.model.Patient;
import com.airwin.repository.PatientRepository;

import jakarta.transaction.Transactional;

@Service
public class PatientService {
    private PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public String save(Patient patient) {

        patientRepository.save(patient);
        return "Patient data saved successfully";
    }

    public Patient findById(int id) {
        return patientRepository.findByPatientid(id);
    }

    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    @Transactional
    public void deleteById(int id) {
        patientRepository.deleteByPatientid(id);
    }

    public Patient findByNames(String firstname, String lastname) {
        return patientRepository.findByFirstnameAndLastname(firstname, lastname);
    }

    public Map<String, String> validatePatient(Patient patient) {
        Map<String, String> errors = new HashMap<>();
        if (patient.getFirstname().isBlank()) {
            errors.put("firstname", "firstname can't be blank");
        }
        if (patient.getLastname().isBlank()) {
            errors.put("lastname", "lastname can't be blank");
        }
        if (patient.getGender() == null) {
            errors.put("gender", "gender can't be null");
        }
        if (patient.getBirthdate() == null) {
            errors.put("birthdate", "birthdate is mondatory");
        }
        return errors;

    }
}
