package com.airwin.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.airwin.model.Patient;
import com.airwin.service.PatientService;

@RestController
@RequestMapping("/api/patient")
public class PatientController {
    private PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Patient patient) {
        Map<String, String> errors = patientService.validatePatient(patient);
        if (!errors.isEmpty()) 
            return ResponseEntity.badRequest().body(errors);
        else
            return ResponseEntity.ok(patientService.save(patient));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam("patientid") int id) {
        patientService.deleteById(id);
        return ResponseEntity.ok("Patient deleted successfully");
    }

    @GetMapping("/getById")
    public ResponseEntity<Patient> findById(@RequestParam int id) {
        return ResponseEntity.ok(patientService.findById(id));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Patient>> findAll() {
        return ResponseEntity.ok(patientService.findAll());
    }

    @GetMapping("/findPatientByNames")
    public ResponseEntity<Patient> findByNames(@RequestParam("firstname") String firstname,
            @RequestParam("firstname") String lastname) {
        return ResponseEntity.ok(patientService.findByNames(firstname, lastname));
    }
}
