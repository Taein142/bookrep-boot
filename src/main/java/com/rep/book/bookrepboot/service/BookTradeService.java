package com.rep.book.bookrepboot.service;

import com.rep.book.bookrepboot.dao.BookDao;
import com.rep.book.bookrepboot.dao.BookTradeDao;
import com.rep.book.bookrepboot.dao.TradeMsgDao;
import com.rep.book.bookrepboot.dto.BookDTO;
import com.rep.book.bookrepboot.dto.BookTradeDTO;
import com.rep.book.bookrepboot.dto.MsgDTO;
import com.rep.book.bookrepboot.dto.PageDTO;
import com.rep.book.bookrepboot.util.MainUtil;
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
public class BookTradeService {
    @Autowired
    BookDao bookDao;
    @Autowired
    BookTradeDao bookTradeDao;
    @Autowired
    TradeMsgDao tradeMsgDao;

    public List<BookDTO> getBookByDB(String loggedInUserEmail, String keyword) {
        log.info("getBookByDB()");
        List<BookDTO> bookList = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("email", loggedInUserEmail);
        map.put("keyword", keyword);

        try {
            bookList = bookDao.getOwnBookByKeyword(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookList;
    }

    public String saveTradeRegister(BookTradeDTO bookTradeDTO, RedirectAttributes rttr) {
        log.info("service-saveTradeResister()");

        String msg = null;
        String view = null;

        try {
            bookTradeDao.registerTrade(bookTradeDTO);
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

    public List<PageDTO> getTradeListByKeyword(String keyword) {
        log.info("getAllTrade()");
        List<BookTradeDTO> allBookTradeList = bookTradeDao.getBookTradeByKeyword(keyword);
        List<Long> ongoingTradeIDs = tradeMsgDao.getTradingID();
        List<Object> SHlist = new ArrayList<>();

        for (BookTradeDTO bookTradeDTO : allBookTradeList) {
            boolean isOngoing = false;
            for (Long id : ongoingTradeIDs) {
                if (bookTradeDTO.getBook_trade_id().equals(id)) {
                    isOngoing = true;
                    break;
                }
            }
            if (!isOngoing) {
                Map<String, Object> map = new HashMap<>();
                try {
                    String isbn = bookTradeDTO.getBook_isbn();
                    BookDTO book = bookDao.getBookByIsbn(isbn);
                    map.put("bookTrade", bookTradeDTO);
                    map.put("book", book);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SHlist.add(map);
            }
        }

        log.info("SHlist {}", SHlist);

        return MainUtil.setPaging(SHlist, 5);
    }

    public BookTradeDTO getInfoById(Long id) {
        log.info("getInfoById()");
        BookTradeDTO bookTradeDTO = bookTradeDao.getInfoById(id);
        log.info("bookTradeDTO {}", bookTradeDTO);

        return bookTradeDTO;
    }
}

