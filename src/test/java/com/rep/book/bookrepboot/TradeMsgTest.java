package com.rep.book.bookrepboot;

import com.rep.book.bookrepboot.dto.MsgDTO;
import com.rep.book.bookrepboot.service.TradeMsgService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

@SpringBootTest
public class TradeMsgTest {
    @Autowired
    TradeMsgService tradeMsgService;

    @Test
    public void sendTradeMsgTest(){
        MsgDTO test = new MsgDTO();
        test.setBook_trade_id(15L);
        test.setSent_user_email("test02@naver.com");
        test.setSent_book_isbn("9781506292380");
        test.setReceived_user_email("test01@naver.com");
        test.setReceived_book_isbn("9788965795223");
        RedirectAttributes rttr = new RedirectAttributesModelMap();
        tradeMsgService.sendTradeMsg(test, rttr);
        System.out.println(rttr.getFlashAttributes());
    }

    @Test
    public void getMsgByIdTest(){
        Long testId = 1L;
        MsgDTO test = tradeMsgService.getMsgByID(testId);
        System.out.println(test);
    }

    @Test
    public void updateTradeMsgStatusTest(){
        Long testId = 4L;
        String testEmail = "test01@naver.com";
        boolean testResult = tradeMsgService.updateTradeMsgStatus(testId, testEmail,1);
        System.out.println(testResult);
    }

}
