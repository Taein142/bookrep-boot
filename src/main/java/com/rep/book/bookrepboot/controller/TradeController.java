package com.rep.book.bookrepboot.controller;

import com.rep.book.bookrepboot.dto.BookDTO;
import com.rep.book.bookrepboot.dto.BookTradeDTO;
import com.rep.book.bookrepboot.dto.PageDTO;
import com.rep.book.bookrepboot.service.TradeService;
import com.rep.book.bookrepboot.util.MainUtil;
import com.rep.book.bookrepboot.util.SecurityUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @GetMapping("user/book-select")
    public String bookSelect(@RequestParam(value = "keyword", required = false) String keyword,
                             @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                             int checkNum, Model model) {
        log.info("bookSelect");

        String loggedInUserEmail = SecurityUtil.getCurrentUserEmail();
        List<BookDTO> bookList = tradeService.getBookByDB(loggedInUserEmail, keyword);
        List<PageDTO> bookPageList = MainUtil.setPaging(bookList,6);

        pageNum = Math.min(pageNum, bookPageList.size());

        model.addAttribute("bookList", bookPageList.get(pageNum - 1));
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPageNum", pageNum);
        model.addAttribute("checkNum", checkNum);
        model.addAttribute("bookListSize", bookPageList.size());

        return "th/bookSelect";
    }

    @GetMapping("apply-book-select")
    public String applyBookSelect(@RequestParam("checkNum") int checkNum, BookDTO bookDTO, RedirectAttributes rttr){
        log.info("applyBookSelect");
        rttr.addFlashAttribute("book", bookDTO);
        log.info("bookDTO {}", bookDTO);

        if (checkNum == 1){
            return "redirect:/trade-resister";
        } else if (checkNum == 2){
            return "redirect:/trade-request";
        } else {
            return "redirect:/share-house";
        }
    }

    @GetMapping("trade-resister")
    public String showTradeResister(@ModelAttribute("bookTrade") BookTradeDTO bookTradeDTO, Model model){
        String email = SecurityUtil.getCurrentUserEmail();
        model.addAttribute("userEmail", email);
        log.info(email);
        return "th/tradeRegistration";
    }

    @PostMapping("save-resister")
    public String saveTradeResister(BookTradeDTO bookTradeDTO, RedirectAttributes rttr){
        log.info("saveTradeResister");

        String view = tradeService.saveTradeRegister(bookTradeDTO, rttr);

        return view;
    }

    @GetMapping("share-house")
    public String showShareHouse(){
        log.info("showShareHouse()");

        return "th/shareHouse";
    }



}
