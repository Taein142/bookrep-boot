package com.rep.book.bookrepboot.service;

import com.rep.book.bookrepboot.dao.BookDao;
import com.rep.book.bookrepboot.dao.BookTradeDao;
import com.rep.book.bookrepboot.dao.TradeMsgDao;
import com.rep.book.bookrepboot.dto.BookDTO;
import com.rep.book.bookrepboot.dto.BookTradeDTO;
import com.rep.book.bookrepboot.dto.MsgDTO;
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
    public List<Object> getReceivedTradeMsg(String loggedInUserEmail) {
        log.info("getReceivedTradeMsg()");
         List<MsgDTO> receivedMsg = tradeMsgDao.getReceivedTradeMsg(loggedInUserEmail);

         return tradeService.addBookInfoList(receivedMsg ,1);
    }

    // 보낸 교환 메시지 가져오는 메소드
    public List<Object> getSentTradeMsg(String loggedInUserEmail) {
        log.info("getSentTradeMsg()");
        List<MsgDTO> sentMsg = tradeMsgDao.getSentTradeMsg(loggedInUserEmail);
        return tradeService.addBookInfoList(sentMsg,2);
    }

    // 교환 등록한 데이터들 가져오는 메서드
    public List<Object> getRegisterList(String loggedInUserEmail) {
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

        return rList;
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
}
