package com.airwin.repository;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.airwin.model.Patient;

@RepositoryDefinition(domainClass = Patient.class, idClass = Integer.class)
public interface PatientRepository  {
    public Patient save(Patient patient);
    public Patient findByPatientid(int patientid);
    public void deleteByPatientid(int patientid);
    public Patient findByFirstnameAndLastname(String firstname, String lastname);
    public List<Patient> findAll();

}
