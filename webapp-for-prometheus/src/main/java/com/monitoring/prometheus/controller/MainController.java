package com.monitoring.prometheus.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @GetMapping("/")
    public String doMain() {
        return "[Monitoring] Hello. This is Prototype Project Main!";
    }

    @GetMapping("/endpoint1")
    public String doEndPoint1() {
        return "[Monitoring] EndPoint 1";
    }

    @GetMapping("/endpoint2")
    public String doEndPoint2() {
        return "[Monitoring] EndPoint 2";
    }

}
