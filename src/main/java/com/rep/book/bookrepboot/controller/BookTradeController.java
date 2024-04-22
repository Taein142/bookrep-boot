package com.rep.book.bookrepboot.controller;

import com.rep.book.bookrepboot.dto.BookDTO;
import com.rep.book.bookrepboot.dto.BookTradeDTO;
import com.rep.book.bookrepboot.dto.PageDTO;
import com.rep.book.bookrepboot.service.BookService;
import com.rep.book.bookrepboot.service.BookTradeService;
import com.rep.book.bookrepboot.util.MainUtil;
import com.rep.book.bookrepboot.util.SecurityUtil;
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
public class BookTradeController {
    @Autowired
    private BookTradeService bookTradeService;
    @Autowired
    private BookService bookService;

    @GetMapping("user/book-select")
    public String bookSelect(@RequestParam(value = "keyword", required = false) String keyword,
                             @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                             @RequestParam(value = "id", required = false) Long id,
                             int checkNum, Model model) {
        log.info("bookSelect");

        String loggedInUserEmail = SecurityUtil.getCurrentUserEmail();
        List<BookDTO> bookList = bookTradeService.getBookByDB(loggedInUserEmail, keyword);
        List<PageDTO> bookPageList = MainUtil.setPaging(bookList,6);

        pageNum = Math.min(pageNum, bookPageList.size());

        model.addAttribute("bookList", bookPageList.get(pageNum - 1));
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPageNum", pageNum);
        model.addAttribute("checkNum", checkNum);
        model.addAttribute("id", id);
        model.addAttribute("bookListSize", bookPageList.size());

        return "th/bookSelect";
    }

    @GetMapping("user/apply-book-select")
    public String applyBookSelect(@RequestParam("checkNum") int checkNum,
                                  @RequestParam(value = "id", required = false) Long id,
                                  BookDTO bookDTO, RedirectAttributes rttr){
        log.info("applyBookSelect");
        rttr.addFlashAttribute("book", bookDTO);
        log.info("bookDTO {}", bookDTO);

        if (checkNum == 1){
            return "redirect:trade-resister";
        } else if (checkNum == 2){
            return "redirect:trade-application?id="+id;
        } else {
            return "redirect:share-house";
        }
    }

    @GetMapping("user/trade-resister")
    public String showTradeResister(@ModelAttribute("bookTrade") BookTradeDTO bookTradeDTO, Model model){
        String email = SecurityUtil.getCurrentUserEmail();
        model.addAttribute("userEmail", email);
        log.info(email);
        return "th/tradeRegistration";
    }

    @PostMapping("user/save-resister")
    public String saveTradeResister(BookTradeDTO bookTradeDTO, RedirectAttributes rttr){
        log.info("saveTradeResister");

        String view = bookTradeService.saveTradeRegister(bookTradeDTO, rttr);

        return view;
    }

    @GetMapping("user/share-house")
    public String showShareHouse(@RequestParam(value = "keyword", required = false) String keyword,
                                 @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                 Model model){
        log.info("showShareHouse()");

        List<PageDTO> getTradeListByKeyword = bookTradeService.getTradeListByKeyword(keyword);
        log.info("allTradeList {}", getTradeListByKeyword);
        model.addAttribute("tradeList", getTradeListByKeyword);
        model.addAttribute("currentPageNum", pageNum);
        model.addAttribute("keyword", keyword);
        model.addAttribute("tradeListSize", getTradeListByKeyword.size());

        return "th/shareHouse";
    }

    @GetMapping("user/trade-application")
    public String showTradeApplication(@RequestParam("id") Long id, Model model){
        log.info("showTradeApplication()");

        BookTradeDTO firstTradeInfo = bookTradeService.getInfoById(id);
        model.addAttribute("firstTrade",firstTradeInfo);
        log.info("firstTradeInfo {}", firstTradeInfo);
        BookDTO firstBook = bookService.getBookByIsbn(firstTradeInfo.getBook_isbn());
        model.addAttribute("firstBook", firstBook);
        log.info("firstBook {}", firstBook);
        model.addAttribute("id",id);
        String email = SecurityUtil.getCurrentUserEmail();
        model.addAttribute("userEmail", email);
        return "th/tradeApplication";
    }

    @PostMapping("/user/send-trade-msg")
    public String sendTradeMsg(@RequestParam("fir_trade_id") Long firTradeId,
                                   @RequestParam("fir_user_email") String firUserEmail,
                                   @RequestParam("sec_user_email") String secUserEmail,
                                   @RequestParam("fir_book_isbn") String firBookIsbn,
                                   @RequestParam("sec_book_isbn") String secBookIsbn,
                                   Model model) {


        return null;
    }
}