package com.rep.book.bookrepboot.service;

import com.rep.book.bookrepboot.dao.BookDao;
import com.rep.book.bookrepboot.dao.BookTradeDao;
import com.rep.book.bookrepboot.dao.TradeMsgDao;
import com.rep.book.bookrepboot.dto.BookDTO;
import com.rep.book.bookrepboot.dto.BookTradeDTO;
import com.rep.book.bookrepboot.dto.MsgDTO;
import com.rep.book.bookrepboot.dto.PageDTO;
import com.rep.book.bookrepboot.util.MainUtil;
import com.rep.book.bookrepboot.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
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

    // db에서 교환중이 아니며, 독후감을 작성하였던 책을 가져오는 메서드
    public List<Map<String, Object>> getUnTradeBookByDB(String loggedInUserEmail, String keyword) {
        log.info("getBookByDB()");
        List<Map<String, Object>> ownBookList = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("email", loggedInUserEmail);
        map.put("keyword", keyword);

        try {
            ownBookList = bookDao.getOwnBookByKeyword(map);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ownBookList;
    }

    // 교환 등록 서비스 메서드
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

    // 키워드에 따른 트레이드 리스트를 가져옴
    public List<PageDTO> getTradeListByKeyword(String keyword) {
        log.info("getAllTrade()");
        String loggedInUserEmail = SecurityUtil.getCurrentUserEmail();
        Map<String, String> map2 = new HashMap<>();
        map2.put("email", loggedInUserEmail);
        map2.put("keyword", keyword);
        List<BookTradeDTO> allBookTradeList = bookTradeDao.getBookTradeByKeyword(map2);
        List<Long> ongoingTradeIDs = tradeMsgDao.getTradingID();
        List<Object> SHlist = new ArrayList<>();

        for (BookTradeDTO bookTradeDTO : allBookTradeList) {
            boolean isOngoing = false; // 교환 메시지를 보낸 데이터를 걸러냄
            for (Long id : ongoingTradeIDs) {
                if (bookTradeDTO.getBook_trade_id().equals(id)) {
                    isOngoing = true;
                    break;
                }
            }
            if (!isOngoing) { // 교환 진행중이 아니라면 book_trade와 book 데이터를 넣어줌
                Map<String, Object> map = new HashMap<>();
                try {
                    String isbn = bookTradeDTO.getBook_isbn();
                    BookDTO book = bookDao.getBookByIsbn(isbn);
                    map.put("bookTrade", bookTradeDTO);
                    map.put("book", book);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SHlist.add(map); // 그것들을 리스트로 만듦.
            }
        }

        log.info("SHlist {}", SHlist);

        return MainUtil.setPaging(SHlist, 5);
    }

    // id를 참고하여 bookTrade 데이터를 가져옴
    public BookTradeDTO getInfoById(Long id) {
        log.info("getInfoById()");
        BookTradeDTO bookTradeDTO = bookTradeDao.getInfoById(id);
        log.info("bookTradeDTO {}", bookTradeDTO);

        return bookTradeDTO;
    }
}

