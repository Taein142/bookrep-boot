package com.rep.book.bookrepboot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class FooterController {
    @GetMapping("privacy-policy")
    public String showPrivacyPolicy(){
        log.info("showPrivacyPolicy()");
        return "th/privacyPolicy";
    }

    @GetMapping("terms")
    public String showTerms(){
        log.info("showTerms");
        return "th/terms";
    }
}
