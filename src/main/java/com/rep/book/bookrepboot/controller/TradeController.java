package com.rep.book.bookrepboot.controller;

import com.rep.book.bookrepboot.dto.PageDTO;
import com.rep.book.bookrepboot.service.FeedService;
import com.rep.book.bookrepboot.service.TradeService;
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
public class TradeController {

    @Autowired
    private TradeService tradeService;
    @Autowired
    private FeedService feedService;

    @GetMapping("user/my-trade-index")
    public String showTradeIndex(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, Model model){
        String loggedInUserEmail = SecurityUtil.getCurrentUserEmail();
        List<PageDTO> tradeIndextList = tradeService.getTradeListByEmail(loggedInUserEmail);
        String userName = feedService.getNameByEmail(loggedInUserEmail);
        model.addAttribute("tradeList" , tradeIndextList.get(pageNum-1));
        model.addAttribute("user", loggedInUserEmail);
        model.addAttribute("currentPageNum", pageNum);
        model.addAttribute("userName", userName);
        return "th/myTradeIndex";
    }
}
