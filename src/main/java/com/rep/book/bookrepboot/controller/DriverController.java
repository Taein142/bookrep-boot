package com.rep.book.bookrepboot.controller;

import com.rep.book.bookrepboot.service.DriverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@Slf4j
@Controller
public class DriverController {

    @Autowired
    private DriverService driverService;


    @GetMapping("driver/delivery")
    public String showDelivery(Model model){
        log.info("showDelivery()");

        driverService.getDeliveryAmount(model);

        return "th/delivery";
    }

    @GetMapping("driver/delivery-detail")
    public String deliveryDetail(@RequestParam("path-id") Long pathId, Model model) throws IOException, ParserConfigurationException, InterruptedException {
        log.info("deliveryDetail()");

        driverService.showDelivery(model, pathId);

        return "th/deliveryDetail";
    }

    @PostMapping("driver/complete-delivery")
    public String completeDelivery(@RequestParam(value = "pathId") Long pathId){
        log.info("completeDelivery()");

        driverService.completeDelivery(pathId);

        return "redirect:/driver/delivery";
    }

}
