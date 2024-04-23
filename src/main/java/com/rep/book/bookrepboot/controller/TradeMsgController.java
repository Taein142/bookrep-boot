package com.rep.book.bookrepboot.controller;

import com.rep.book.bookrepboot.dto.MsgDTO;
import com.rep.book.bookrepboot.service.TradeMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
public class TradeMsgController {

    @Autowired
    TradeMsgService tradeMsgService;

    @PostMapping("user/send-trade-msg")
    public String sendTradeMsg(MsgDTO msgDTO, RedirectAttributes rttr) {
        log.info("sendTradeMsg()");

        return tradeMsgService.sendTradeMsg(msgDTO, rttr);
    }

    // 승낙 거절 기능 만들기
}
