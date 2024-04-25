package com.rep.book.bookrepboot.service;

import com.rep.book.bookrepboot.dao.BookDao;
import com.rep.book.bookrepboot.dao.TradeDao;
import com.rep.book.bookrepboot.dao.TradeMsgDao;
import com.rep.book.bookrepboot.dto.BookDTO;
import com.rep.book.bookrepboot.dto.BookTradeDTO;
import com.rep.book.bookrepboot.dto.MsgDTO;
import com.rep.book.bookrepboot.dto.TradeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TradeMsgService {
    @Autowired
    private TradeMsgDao tradeMsgDao;
    @Autowired
    private TradeDao tradeDao;
    @Autowired
    private PlatformTransactionManager manager;
    @Autowired
    private TransactionDefinition definition;
    @Autowired
    private BookDao bookDao;

    public String saveTradeMsg(MsgDTO msgDTO, RedirectAttributes rttr) {
        log.info("saveTradMsg() - service");
        String msg = null;
        String view = null;

        try{
            tradeMsgDao.setTradeMsg(msgDTO);
            msg = "신청 메시지 전송 완료";
        }catch (Exception e){
            e.printStackTrace();
            msg = "신청 메시지 전송 실패";
        }
        view = "redirect:share-house";
        rttr.addFlashAttribute("msg", msg);

        return view;
    }

    public MsgDTO getMsgByID(Long msgId) {
        log.info("getMsgById()");
        MsgDTO msgDTO = tradeMsgDao.getMsgById(msgId);
        log.info("msgDTO {}", msgDTO);

        return msgDTO;
    }

    // msg status 업테이트 메서드
    public boolean updateTradeMsgStatus(Long msgId, String userEmail, int status) {
        log.info("updateTradeMsgStatus()");
        TransactionStatus transactionStatus = manager.getTransaction(definition);

        MsgDTO msgDTO = tradeMsgDao.getMsgById(msgId);
        boolean result = false;

        if (msgDTO != null && msgDTO.getReceived_user_email().equals(userEmail)) {
            try {
                Map<String, Object> map = new HashMap<>();
                map.put("msgId", msgId);
                map.put("status", status);
                result = tradeMsgDao.updateMsgStatus(map);
                log.info("update msg_status");

                if (status == 1){
                    TradeDTO tradeDTO = new TradeDTO();
                    tradeDTO.setFir_user_email(msgDTO.getSent_user_email());
                    tradeDTO.setSec_user_email(msgDTO.getReceived_user_email());
                    tradeDTO.setFir_book_isbn(msgDTO.getSent_book_isbn());
                    tradeDTO.setSec_book_isbn(msgDTO.getReceived_book_isbn());
                    tradeDTO.setTrade_status(0);

                    tradeDao.setTrade(tradeDTO);
                }

                manager.commit(transactionStatus);
            } catch (Exception e) {
                e.printStackTrace();
                manager.rollback(transactionStatus);
                return false;
            }
        }
        return result;
    }

    // 책 정보를 조합하여 리턴하는 메서드
    public List<Object> addBookInfo(List<MsgDTO> msglist){
        List<Object> mList = new ArrayList<>();

        try{
            for (MsgDTO msgDTO : msglist) {
                Map<String, Object> map = new HashMap<>();
                String receivedIsbn = msgDTO.getReceived_book_isbn();
                String sentIsbn = msgDTO.getSent_book_isbn();
                BookDTO receivedBook = bookDao.getBookByIsbn(receivedIsbn);
                BookDTO sentBook = bookDao.getBookByIsbn(sentIsbn);
                map.put("msg", msgDTO);
                map.put("receivedBook", receivedBook);
                map.put("sentBook", sentBook);
                mList.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mList;
    }
}
