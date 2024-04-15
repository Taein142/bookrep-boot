package com.rep.book.bookrepboot.controller;

import com.rep.book.bookrepboot.dto.UserDTO;
import com.rep.book.bookrepboot.service.MailService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping("mailConfirm")
    public String mailConfirm(UserDTO userDTO, HttpSession session) {
        log.info("mailConfirm()");
        String res = mailService.sendEmail(userDTO, session);

        return res;
    }

    @PostMapping("codeAuth")
    public String codeAuth(@RequestParam("vCode") String vCode,
                           HttpSession session){
        log.info("codeAuth()");

        String res = mailService.codeAuth(vCode, session);

        return res;
    }

}
