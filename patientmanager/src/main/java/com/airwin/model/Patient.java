package com.airwin.model;


import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer patientid;
    private String firstname;
    private String lastname;
    @Enumerated(EnumType.STRING)
    private PatientGender gender;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthdate;
    private String phone;
    private String zipcode;
    public Patient(){

    }
    public Integer getPatientid() {
        return patientid;
    }
    public void setPatientid(Integer id) {
        this.patientid = id;
    }
    public PatientGender getGender() {
        return gender;
    }

    public void setGender(PatientGender gender) {
        this.gender = gender;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

}
