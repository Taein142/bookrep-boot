package com.rep.book.bookrepboot.controller;

import com.rep.book.bookrepboot.service.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


@Controller
@Slf4j
public class TradeController {

    @Autowired
    private TradeService tradeService;

}
