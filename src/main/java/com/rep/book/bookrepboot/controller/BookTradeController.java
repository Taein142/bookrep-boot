package com.rep.book.bookrepboot.controller;

import com.rep.book.bookrepboot.dto.BookDTO;
import com.rep.book.bookrepboot.dto.BookTradeDTO;
import com.rep.book.bookrepboot.dto.PageDTO;
import com.rep.book.bookrepboot.service.BookService;
import com.rep.book.bookrepboot.service.BookTradeService;
import com.rep.book.bookrepboot.service.FeedService;
import com.rep.book.bookrepboot.util.MainUtil;
import com.rep.book.bookrepboot.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class BookTradeController {
    @Autowired
    private BookTradeService bookTradeService;
    @Autowired
    private BookService bookService;
    @Autowired
    private FeedService feedService;

    @GetMapping("user/book-select") // 교환 등록 및 신청시 책 선택 페이지
    public String bookSelect(@RequestParam(value = "checkNum") int checkNum,
                             @RequestParam(value = "keyword", required = false) String keyword,
                             @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                             @RequestParam(value = "id", required = false) Long id,
                             Model model) {
        log.info("bookSelect");

        String loggedInUserEmail = SecurityUtil.getCurrentUserEmail(); // 시큐리티에서 현재 로그인 한 사람 가져옴.
        List<Map<String, Object>> bookList = bookTradeService.getUnTradeBookByDB(loggedInUserEmail, keyword);
        List<PageDTO> bookPageList = MainUtil.setPaging(bookList,3);
        String userName = feedService.getNameByEmail(loggedInUserEmail);

        pageNum = Math.min(pageNum, bookPageList.size());

        if (bookList.size()>0){
            model.addAttribute("bookList", bookPageList.get(pageNum-1));
        } else {
            model.addAttribute("bookList", null);
        }

        model.addAttribute("userEmail", loggedInUserEmail);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPageNum", pageNum);
        model.addAttribute("checkNum", checkNum);
        model.addAttribute("id", id);
        model.addAttribute("bookListSize", bookPageList.size());
        model.addAttribute("userName", userName);

        return "th/bookSelect";
    }

    @GetMapping("user/apply-book-select") // 책 선택 적용
    public String applyBookSelect(@RequestParam("checkNum") int checkNum,
                                  @RequestParam(value = "id", required = false) Long id,
                                  BookDTO bookDTO, Long report_id, RedirectAttributes rttr){
        log.info("applyBookSelect");
        rttr.addFlashAttribute("book", bookDTO);
        rttr.addFlashAttribute("report_id", report_id);
        log.info("bookDTO {}", bookDTO);
        log.info("report_id {}", report_id);

        if (checkNum == 1){ // 파라미터로 받은 checkNum에 따라 각각의 페이지로 리턴
            return "redirect:trade-resister";
        } else if (checkNum == 2){
            return "redirect:trade-application?id="+id;
        } else {
            return "redirect:share-house";
        }
    }

    @GetMapping("user/trade-resister") //교환 책 등록 페이지
    public String showTradeResister(@ModelAttribute("bookTrade") BookTradeDTO bookTradeDTO, Model model){
        log.info("showTradeResister()");

        String loggedInUserEmail = SecurityUtil.getCurrentUserEmail();
        String userName = feedService.getNameByEmail(loggedInUserEmail);

        model.addAttribute("userName", userName);
        model.addAttribute("userEmail", loggedInUserEmail);

        log.info(loggedInUserEmail);
        return "th/tradeRegistration";
    }

    @PostMapping("user/save-resister") // 교환하고픈 책 등록
    public String saveTradeResister(BookTradeDTO bookTradeDTO, RedirectAttributes rttr){
        log.info("saveTradeResister");
        log.info("bookTradeDTO {}", bookTradeDTO);
        String view = bookTradeService.saveTradeRegister(bookTradeDTO, rttr);

        return view;
    }

    @GetMapping("user/share-house") // shareHouse 페이지
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

        String loggedInUserEmail = SecurityUtil.getCurrentUserEmail();
        String userName = feedService.getNameByEmail(loggedInUserEmail);
        model.addAttribute("userName", userName);
        model.addAttribute("userEmail", loggedInUserEmail);

        return "th/shareHouse";
    }

    @GetMapping("user/trade-application") //교환 신청 페이지
    public String showTradeApplication(@RequestParam("id") Long id, Model model){
        log.info("showTradeApplication()");

        // id를 참조하여 book_trade 에서 데이터 가져옴.
        BookTradeDTO firstTradeInfo = bookTradeService.getInfoById(id);
        model.addAttribute("firstTrade",firstTradeInfo);
        log.info("firstTradeInfo {}", firstTradeInfo);

        // 가져온 book_trade 정보를 참고하여 책의 정보를 가져옴.
        BookDTO firstBook = bookService.getBookByIsbn(firstTradeInfo.getBook_isbn());
        model.addAttribute("firstBook", firstBook);
        log.info("firstBook {}", firstBook);
        model.addAttribute("id",id);

        String loggedInUserEmail = SecurityUtil.getCurrentUserEmail();
        String userName = feedService.getNameByEmail(loggedInUserEmail);

        model.addAttribute("userName", userName);
        model.addAttribute("userEmail", loggedInUserEmail);

        return "th/tradeApplication";
    }
}