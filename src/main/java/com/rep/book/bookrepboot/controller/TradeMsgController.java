package com.rep.book.bookrepboot.controller;

import com.rep.book.bookrepboot.dto.MsgDTO;
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

    @PostMapping("user/send-trade-msg")
    public String sendTradeMsg(MsgDTO msgDTO, RedirectAttributes rttr) {
        log.info("sendTradeMsg()");

        return tradeMsgService.sendTradeMsg(msgDTO, rttr);
    }

    @PostMapping("user/accept")
    @ResponseBody
    public boolean acceptTrade(Long msgId) {
        String loggedEmail = SecurityUtil.getCurrentUserEmail();
        return tradeMsgService.updateTradeMsgStatus(msgId, loggedEmail,1);
    }

    @PostMapping("user/reject")
    @ResponseBody
    public boolean rejectTrade(Long msgId) {
        String loggedEmail = SecurityUtil.getCurrentUserEmail();
        return tradeMsgService.updateTradeMsgStatus(msgId, loggedEmail,2);
    }

    // 취소 기능 만들 예정

    // 이거 sender, receiver 입장으로 페이지를 따로 나눈다고 하므로 수정해야 됨
    // 한 파일에서 if 문으로 나누지 않고 페이지를 따로 하고픈 것으로 보임.
    @GetMapping("user/msg-detail")
    public String showMsgDetail(@RequestParam("id") Long msgId, Model model) {
        String email = SecurityUtil.getCurrentUserEmail();

        MsgDTO msgDTO = tradeMsgService.getMsgByID(msgId);
        model.addAttribute("msgDTO", msgDTO);
        model.addAttribute("loggedEmail", email);

        return "th/myTradeApplication";
    }
}