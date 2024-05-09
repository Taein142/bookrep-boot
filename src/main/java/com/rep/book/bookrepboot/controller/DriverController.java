package com.rep.book.bookrepboot.controller;

import com.rep.book.bookrepboot.service.DriverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class DriverController {

    @Autowired
    private DriverService driverService;


    @GetMapping("driver/delivery")
    public String showDelivery(Model model){
        log.info("showDelivery()");

        driverService.showDelivery(model);

        return "th/delivery";
    }

    @PostMapping("driver/complete-delivery")
    public String completeDelivery(@RequestParam(value = "pathId") Long pathId){
        log.info("completeDelivery()");

        driverService.completeDelivery(pathId);

        return "redirect:/driver/delivery";
    }

}
