package com.rep.book.bookrepboot.service;

import com.rep.book.bookrepboot.dao.BookDao;
import com.rep.book.bookrepboot.dao.TradeDao;
import com.rep.book.bookrepboot.dto.BookDTO;
import com.rep.book.bookrepboot.dto.BookTradeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TradeService {
    @Autowired
    BookDao bookDao;
    @Autowired
    TradeDao tradeDao;

    public List<BookDTO> getBookByDB(String loggedInUserEmail, String keyword) {
        log.info("getBookByDB()");
        List<BookDTO> bookList = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("email", loggedInUserEmail);
        map.put("keyword", keyword);

        try {
            bookList = bookDao.getOwnBookByKeyword(map);
        } catch (Exception e){
            e.printStackTrace();
        }
        return bookList;
    }

    public String saveTradeRegister(BookTradeDTO bookTradeDTO, RedirectAttributes rttr) {
        log.info("service-saveTradeResister()");

        String msg = null;
        String view = null;

        try {
            tradeDao.registerTrade(bookTradeDTO);
            msg = "등록 성공";
            view = "redirect:share-house";
        } catch (Exception e) {
            e.printStackTrace();
            msg = "등록 실패";
            view = "redirect:trade-resister";
        }

        rttr.addFlashAttribute("msg", msg);
        return view;
    }
}
