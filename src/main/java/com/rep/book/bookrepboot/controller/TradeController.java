package com.rep.book.bookrepboot.controller;

import com.rep.book.bookrepboot.service.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@Slf4j
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @GetMapping("/user/trade-application")
    public String showTradeApplication(@RequestParam("id") Long book_trade_id){
        log.info("showTradeApplication()");

        return "tradeApplication";
    }

}
