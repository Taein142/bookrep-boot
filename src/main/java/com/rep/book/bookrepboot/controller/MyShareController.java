package com.rep.book.bookrepboot.controller;

import com.rep.book.bookrepboot.dto.PageDTO;
import com.rep.book.bookrepboot.service.MyShareService;
import com.rep.book.bookrepboot.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Slf4j
public class MyShareController {
    @Autowired
    MyShareService myShareService;

    @GetMapping("user/my-share")
    public String showMyShare(Model model){
        log.info("showMyShare()");
        String loggedInUserEmail = SecurityUtil.getCurrentUserEmail();
        List<PageDTO> receivedMessages = myShareService.getReceivedTradeMsg(loggedInUserEmail);
        List<PageDTO> sentMessages = myShareService.getSentTradeMsg(loggedInUserEmail);
        List<PageDTO> registerList = myShareService.getRegisterList(loggedInUserEmail);

        model.addAttribute("registerList", registerList);
        model.addAttribute("sentMsg", sentMessages);
        model.addAttribute("receivedMsg", receivedMessages);
        model.addAttribute("loggedInUserEmail",loggedInUserEmail);

        return "th/myShare";
    }
}
