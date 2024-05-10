package com.rep.book.bookrepboot.controller;

import com.rep.book.bookrepboot.dto.PageDTO;
import com.rep.book.bookrepboot.service.FeedService;
import com.rep.book.bookrepboot.service.MyShareService;
import com.rep.book.bookrepboot.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String showMyShare(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, Model model) {
        log.info("showMyShare()");
        String loggedInUserEmail = SecurityUtil.getCurrentUserEmail();
        List<PageDTO> registerList = myShareService.getRegisterList(loggedInUserEmail); // 교환 등록한 데이터 가져옴

        model.addAttribute("tradeList", registerList); // 내 교환 등록 리스트
        model.addAttribute("userEmail", loggedInUserEmail); //로그인 한 유저 이메일
        model.addAttribute("currentPageNum", pageNum);
        model.addAttribute("listMaxSize", registerList.size());


        return "th/myShare";
    }

    @GetMapping("user/received-requests")
    public String showReceivedRequest(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, Model model) {
        log.info("showMyShare()");
        String loggedInUserEmail = SecurityUtil.getCurrentUserEmail();
        List<PageDTO> receivedMessages = myShareService.getReceivedTradeMsg(loggedInUserEmail); // 받은 메시지 데이터 가져옴

        model.addAttribute("receiveTradeRequestList", receivedMessages); // 교환 신청받은 책 리스트
        model.addAttribute("currentPageNum", pageNum);
        model.addAttribute("userEmail", loggedInUserEmail); //로그인 한 유저 이메일
        model.addAttribute("listMaxSize", receivedMessages.size());

        return "th/receivedRequests";
    }

    @GetMapping("user/trade-requests")
    public String showMakeTradeRequestList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, Model model) {
        log.info("showMakeTradeRequestList()");
        String loggedInUserEmail = SecurityUtil.getCurrentUserEmail();
        List<PageDTO> sentMessages = myShareService.getSentTradeMsg(loggedInUserEmail); // 보낸 메시지 데이터 가져옴

        model.addAttribute("makeTradeRequestList", sentMessages); // 교환 신청한 책 리스트
        model.addAttribute("currentPageNum", pageNum);
        model.addAttribute("userEmail", loggedInUserEmail); //로그인 한 유저 이메일
        model.addAttribute("listMaxSize", sentMessages.size());

        return "th/tradeRequests";
    }

    @PostMapping("user/delete-book-trade")
    @ResponseBody
    public boolean deleteBookTrade(@RequestParam(value = "id") Long id) {
        log.info("deleteBookTrade()");

        return myShareService.deleteTradeRegistration(id);
    }

    @GetMapping("user/my-trade-msgs")
    public String showMyTradeMsgs(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, Model model) {
        log.info("showMyTradeMsgs()");

        String loggedInUserEmail = SecurityUtil.getCurrentUserEmail();
        List<PageDTO> msgList = myShareService.getInProgressMsg(loggedInUserEmail);
        log.info("msgList {}", msgList);

        if (msgList.size() > 0) {
            model.addAttribute("msgList", msgList.get(pageNum - 1));
        }else {
            model.addAttribute("msgList", null);
        }
        model.addAttribute("currentPageNum", pageNum);
        model.addAttribute("userEmail", loggedInUserEmail);
        model.addAttribute("listMaxSize", msgList.size());

        return "th/myTradeMsgs";
    }
}
