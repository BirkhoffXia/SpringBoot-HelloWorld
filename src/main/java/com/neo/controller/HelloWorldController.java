package com.neo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @RequestMapping("/")
    public String index() {
        return "Hello Spring Boot v1.0.0!";
    }
	
    @RequestMapping("/hello")
    public String hello() {
        return "Hello World from MageEdu.com v1.0.0!";
    }
	
    @RequestMapping("/version")
    public String version() {
        return "version v1.0.0!";
    }
}
