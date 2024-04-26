package com.rep.book.bookrepboot.service;


import com.rep.book.bookrepboot.dao.BookDao;
import com.rep.book.bookrepboot.dao.TradeDao;
import com.rep.book.bookrepboot.dao.TradeMsgDao;
import com.rep.book.bookrepboot.dto.BookDTO;
import com.rep.book.bookrepboot.dto.MsgDTO;
import com.rep.book.bookrepboot.dto.PageDTO;
import com.rep.book.bookrepboot.dto.TradeDTO;
import com.rep.book.bookrepboot.util.MainUtil;
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
public class TradeService {
    @Autowired
    TradeDao tradeDao;
    @Autowired
    BookDao bookDao;
    @Autowired
    private TradeMsgDao tradeMsgDao;
    @Autowired
    private PlatformTransactionManager manager;
    @Autowired
    private TransactionDefinition definition;

    // 유저 이메일에 따라 교환중인 리스트를 가져오는 메서드(trade 테이블)
    public List<PageDTO> getTradeListByEmail(String email) {
        log.info("getTradeListByEmail()");
        List<TradeDTO> tradeDTOList = tradeDao.getTradeListByEmail(email);
        List<Object> tradeList = new ArrayList<>();
        for (int i = 0; i < tradeDTOList.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("tradeInfo", tradeDTOList.get(i));
            BookDTO firstBook = bookDao.getBookByIsbn(tradeDTOList.get(i).getFir_book_isbn());
            BookDTO secondBook = bookDao.getBookByIsbn(tradeDTOList.get(i).getSec_book_isbn());
            map.put("firstBook",firstBook);
            map.put("secondBook",secondBook);
            tradeList.add(map);
        }
        log.info("tradeList {}", tradeList);
        return MainUtil.setPaging(tradeList, 10);
    }

    // 교환 신청 보내는 서비스 메서드
    public String saveTradeMsg(MsgDTO msgDTO, RedirectAttributes rttr) {
        log.info("saveTradMsg() - service");
        String msg = null;
        String view = null;

        try{
            tradeMsgDao.setTradeMsg(msgDTO);
            msg = "신청 메시지 전송 완료";
            view = "redirect:my-trade";
        }catch (Exception e){
            e.printStackTrace();
            msg = "신청 메시지 전송 실패";
            view = "redirect:share-house";
        }
        rttr.addFlashAttribute("msg", msg);

        return view;
    }

    // id를 참고하여 교환 메시지 정보를 가져오는 메서드
    public MsgDTO getMsgByID(Long msgId) {
        log.info("getMsgById()");
        MsgDTO msgDTO = tradeMsgDao.getMsgById(msgId);
        log.info("msgDTO {}", msgDTO);

        return msgDTO;
    }

    // msg status 업테이트 메서드
    // 0: 메시지만 보낸 상태  1: 수락  2: 거절  3: 취소
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

                if (status == 1){ // 승낙하였다면 trade 테이블에 정보 저장
                    TradeDTO tradeDTO = new TradeDTO();
                    tradeDTO.setFir_user_email(msgDTO.getSent_user_email());
                    tradeDTO.setSec_user_email(msgDTO.getReceived_user_email());
                    tradeDTO.setFir_book_isbn(msgDTO.getSent_book_isbn());
                    tradeDTO.setSec_book_isbn(msgDTO.getReceived_book_isbn());
                    tradeDTO.setTrade_status(0);
                    tradeDao.setTrade(tradeDTO);
                }
                manager.commit(transactionStatus); // 상태 변경, trade 저장 둘 다 성공하면 진행되도록 함
            } catch (Exception e) {
                e.printStackTrace();
                manager.rollback(transactionStatus); // 둘 중 하나라도 안되면 롤백
                result = false;
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
