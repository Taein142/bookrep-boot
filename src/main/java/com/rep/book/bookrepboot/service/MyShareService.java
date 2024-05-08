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
import scala.xml.include.sax.Main;

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
    @Autowired
    TradeService tradeService;

    // 받은 교환 메시지 가져오는 메서드
    public List<PageDTO> getReceivedTradeMsg(String loggedInUserEmail) {
        log.info("getReceivedTradeMsg()");
         List<MsgDTO> receivedMsg = tradeMsgDao.getReceivedTradeMsg(loggedInUserEmail);

         List<Object> receivedList = tradeService.addBookInfoList(receivedMsg,1);

        return MainUtil.setPaging(receivedList,5);
    }

    // 보낸 교환 메시지 가져오는 메소드
    public List<PageDTO> getSentTradeMsg(String loggedInUserEmail) {
        log.info("getSentTradeMsg()");
        List<MsgDTO> sentMsg = tradeMsgDao.getSentTradeMsg(loggedInUserEmail);
        List<Object> sentList =  tradeService.addBookInfoList(sentMsg,2);
        return MainUtil.setPaging(sentList, 5);
    }

    // 교환 등록한 데이터들 가져오는 메서드
    public List<PageDTO> getRegisterList(String loggedInUserEmail) {
        log.info("getRegister()");
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

    public boolean deleteTradeRegistration(Long id) {
        log.info("deleteTradeRegistration - service");
        boolean result = false;

        try{
            bookTradeDao.deleteTradeRegistration(id);
            log.info("삭제 성공");
            result = true;
        } catch (Exception e){
            e.printStackTrace();
            log.info("삭제 실패");
        }

        return result;
    }

    public List<PageDTO> getInProgressMsg(String loggedInUserEmail) {
        log.info("getInProgressMsg()");
        List<MsgDTO> msgList = tradeMsgDao.getInProgressMsg(loggedInUserEmail);
        List<Object> mList = new ArrayList<>();

        try{
            for (MsgDTO msgDTO : msgList) {
                Map<String, Object> map = new HashMap<>();
                BookDTO receivedBook = bookDao.getBookByIsbn(msgDTO.getReceived_book_isbn());
                BookDTO sentBook = bookDao.getBookByIsbn(msgDTO.getSent_book_isbn());
                map.put("msgDTO", msgDTO);
                map.put("receivedBook", receivedBook);
                map.put("sentBook", sentBook);
                mList.add(map);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return MainUtil.setPaging(mList, 5);
    }
}
