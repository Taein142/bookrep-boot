package com.rep.book.bookrepboot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class SuperAdminController {

    @GetMapping("super-admin/index")
    public String superAdminIndex(){
        log.info("superAdminIndex()");

        return "th/superAdminMain";
    }
}
