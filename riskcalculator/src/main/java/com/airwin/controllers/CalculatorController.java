package com.airwin.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.airwin.services.CalculatorService;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class CalculatorController {
    private CalculatorService calculatorService;

    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }
    @GetMapping("/api/risk/gethealth")
    public ResponseEntity<String> getMethodName(
        @RequestParam("gender") String gender, 
        @RequestParam("age") int age, 
        @RequestParam("symptomecount") int symptoms) {
        String health = calculatorService.getPatientHealth(symptoms, age, gender);
        return ResponseEntity.ok(health);
    }
    
}
