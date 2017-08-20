package com.ak.practice.twassignment.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ak.practice.twassignment.services.HttpHuntService;

@RestController
public class HttpHuntController {
    
    @Autowired
    private HttpHuntService service;
    
    @GetMapping(value="/assignment1/stage4")
    public String assignment1(){
        return service.executeStage4();
    }

}
