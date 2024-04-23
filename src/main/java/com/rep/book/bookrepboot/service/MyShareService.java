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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MyShareService {
    @Autowired
    BookTradeDao bookTradeDao;
    @Autowired
    TradeMsgDao tradeMsgDao;
    @Autowired
    BookDao bookDao;

    public List<PageDTO> getReceivedTradeMsg(String loggedInUserEmail) {
        log.info("getReceivedTradeMsg()");
        List<MsgDTO> receivedTradeMsg = tradeMsgDao.getReceivedTradeMsg(loggedInUserEmail);
        return MainUtil.setPaging(receivedTradeMsg, 5);
    }

    public List<PageDTO> getSentTradeMsg(String loggedInUserEmail) {
        log.info("getSentTradeMsge()");
        List<MsgDTO> sentTradeMsg = tradeMsgDao.getSentTradeMsg(loggedInUserEmail);
        return MainUtil.setPaging(sentTradeMsg, 5);
    }

    public List<PageDTO> getRegisterList(String loggedInUserEmail) {
        log.info("getRegiester()");
        List<BookTradeDTO> registerList = bookTradeDao.getBookTradeByEmail(loggedInUserEmail);
        List<Object> rList = new ArrayList<>();

        try{
            for (BookTradeDTO bookTradeDTO : registerList) {
                Map<String, Object> map = new HashMap<>();
                String isbn = bookTradeDTO.getBook_isbn();
                BookDTO book = bookDao.getBookByIsbn(isbn);
                map.put("bookTrade", bookTradeDTO);
                map.put("book", book);
                rList.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("rList {}", rList);

        return MainUtil.setPaging(rList, 5);
    }
}
