package com.rep.book.bookrepboot.controller;

import com.rep.book.bookrepboot.dao.AdminDao;
import com.rep.book.bookrepboot.dto.AdminDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class AdminSignController {

    @Autowired
    private AdminDao adminDao;

    @GetMapping("admin/sign-in")
    public String showAdminSignIn(){

        return "th/adminSignIn";
    }

    @GetMapping("signsign")
    public String adminSign(){
        log.info("signsign");

        return "th/adminS";
    }

    @PostMapping("adminUp")
    public String adminUp(@RequestParam String password, @RequestParam String role){
        log.info("adminUp");
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        password = bCryptPasswordEncoder.encode(password);
        AdminDTO adminDTO = new AdminDTO(null, password, role);
        adminDao.adminUp(adminDTO);

        return "redirect:/admin/sign-in";
    }

    @GetMapping("admin/index")
    public String adminMain(){
      
        return "th/adminMain";
    }
}
