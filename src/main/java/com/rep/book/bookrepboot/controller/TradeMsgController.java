package com.rep.book.bookrepboot.controller;

import com.rep.book.bookrepboot.dto.MsgDTO;
import com.rep.book.bookrepboot.service.FeedService;
import com.rep.book.bookrepboot.service.TradeMsgService;
import com.rep.book.bookrepboot.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
public class TradeMsgController {

    @Autowired
    private TradeMsgService tradeMsgService;
    @Autowired
    private FeedService feedService;

    @PostMapping("user/send-trade-msg")
    public String sendTradeMsg(MsgDTO msgDTO, RedirectAttributes rttr) {
        log.info("sendTradeMsg()");

        return tradeMsgService.saveTradeMsg(msgDTO, rttr);
    }

    // 0: 메시지만 보낸 상태  1: 수락  2: 거절  3: 취소
    @PostMapping("user/accept-msg") // 수락 기능
    @ResponseBody
    public boolean acceptTrade(@RequestParam("msgId") Long msgId) {
        String loggedEmail = SecurityUtil.getCurrentUserEmail();
        return tradeMsgService.updateTradeMsgStatus(msgId, loggedEmail, 1);
    }

    @PostMapping("user/reject-msg") // 거절 기능
    @ResponseBody
    public boolean rejectTrade(@RequestParam("msgId") Long msgId) {
        String loggedEmail = SecurityUtil.getCurrentUserEmail();
        return tradeMsgService.updateTradeMsgStatus(msgId, loggedEmail, 2);
    }

    @PostMapping("user/cancel-msg") // 취소 기능
    @ResponseBody
    public boolean cancelTrade(@RequestParam("msgId") Long msgId) {
        String loggedEmail = SecurityUtil.getCurrentUserEmail();
        return tradeMsgService.updateTradeMsgStatus(msgId, loggedEmail, 3);
    }

    @GetMapping("user/msg-detail") // 프론트에서 /user/msg-detail?id={msgDTO.msg_id}
    public String showMsgDetail(@RequestParam("id") Long msgId, Model model) {
        String email = SecurityUtil.getCurrentUserEmail();

        MsgDTO msgDTO = tradeMsgService.getMsgByID(msgId);
        String userName = feedService.getNameByEmail(email);

        model.addAttribute("msg", msgDTO);
        model.addAttribute("user", email);
        model.addAttribute("userName", userName);

        if (msgDTO.getSent_user_email().equals(email)) {
            return "th/myTradeApplication"; // 신청 한 사람(취소 기능 있는 페이지)
        } else if (msgDTO.getReceived_user_email().equals(email)) {
            return "th/myTradeRegistration"; // 신청 받은 사람(수락, 거절 기능 있는 페이지)
        } else {
            return "th/shareHouse";
        }
    }
}
