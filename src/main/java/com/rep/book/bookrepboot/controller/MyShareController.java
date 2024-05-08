package com.rep.book.bookrepboot.controller;

import com.rep.book.bookrepboot.service.FeedService;
import com.rep.book.bookrepboot.service.MyShareService;
import com.rep.book.bookrepboot.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j
public class MyShareController {
    @Autowired
    private MyShareService myShareService;
    @Autowired
    private FeedService feedService;

    // myShare 페이지 보여주는 메서드
    @GetMapping("user/my-share")
    public String showMyShare(Model model){
        log.info("showMyShare()");
        String loggedInUserEmail = SecurityUtil.getCurrentUserEmail();
        List<Object> receivedMessages = myShareService.getReceivedTradeMsg(loggedInUserEmail); // 받은 메시지 데이터 가져옴
        List<Object> sentMessages = myShareService.getSentTradeMsg(loggedInUserEmail); // 보낸 메시지 데이터 가져옴
        List<Object> registerList = myShareService.getRegisterList(loggedInUserEmail); // 교환 등록한 데이터 가져옴
        String userName = feedService.getNameByEmail(loggedInUserEmail); // 로그인 중인 유저의 이름 가져옴

        model.addAttribute("tradeList", registerList); // 내 교환 등록 리스트
        model.addAttribute("makeTradeRequestList", sentMessages); // 교환 신청한 책 리스트
        model.addAttribute("receiveTradeRequestList", receivedMessages); // 교환 신청받은 책 리스트
        model.addAttribute("userEmail",loggedInUserEmail); //로그인 한 유저 이메일
        model.addAttribute("userName", userName); // 로그인 한 유저 이름

        return "th/myShare";
    }

    @PostMapping("user/delete-book-trade")
    @ResponseBody
    public boolean deleteBookTrade(@RequestParam(value = "id") Long id){
        log.info("deleteBookTrade()");

        return myShareService.deleteTradeRegistration(id);
    }
}
