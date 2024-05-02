package com.rep.book.bookrepboot.controller;

import com.rep.book.bookrepboot.dto.PageDTO;
import com.rep.book.bookrepboot.service.AdminBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Slf4j
public class AdminBookController {
    @Autowired
    AdminBookService adminBookService;

    @GetMapping("admin/main-book-management")
    public String showBookManagement(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                     @RequestParam(value = "option", defaultValue = "name") String option,
                                     @RequestParam(value = "keyword", required = false) String keyword,
                                     Model model){
        List<PageDTO> bookList = adminBookService.getBookByKeyword(option,keyword);
        log.info("bookList{}", bookList);

        if (bookList.size() > 0){
            model.addAttribute("bookList", bookList.get(pageNum-1));
        } else {
            model.addAttribute("bookList", null);
        }
        model.addAttribute("listMaxSize", bookList.size());
        model.addAttribute("currentPageNum", pageNum);
        model.addAttribute("keyword", keyword);
        model.addAttribute("option", option);
        return "th/bookManagement";

    }
}
