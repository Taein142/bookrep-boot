package com.rep.book.bookrepboot;

import com.rep.book.bookrepboot.dto.MsgDTO;
import com.rep.book.bookrepboot.dto.PageDTO;
import com.rep.book.bookrepboot.service.TradeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.List;

@SpringBootTest
public class TradeTest {
    @Autowired
    private TradeService tradeService;

    @Test
    public void getTradeListByEmailTest(){
        String testEmail = "test01@naver.com";
        List<PageDTO> testList = tradeService.getTradeListByEmail(testEmail);
        System.out.println(testList);
    }

    @Test
    public void sendTradeMsgTest(){
        MsgDTO test = new MsgDTO();
        test.setBook_trade_id(15L);
        test.setSent_user_email("xodlsdldy@naver.com");
        test.setSent_book_isbn("9788957269244");
        test.setReceived_user_email("test02@naver.com");
        test.setReceived_book_isbn("9788998047238");
        RedirectAttributes rttr = new RedirectAttributesModelMap();
        tradeService.saveTradeMsg(test, rttr);
        System.out.println(rttr.getFlashAttributes());
    }

    @Test
    public void getMsgByIdTest(){
        Long testId = 1L;
        MsgDTO test = tradeService.getMsgByID(testId);
        System.out.println(test);
    }

    @Test
    public void updateTradeMsgStatusTest() throws Exception {
        Long testId = 4L;
        String testEmail = "test01@naver.com";
        boolean testResult = tradeService.updateTradeMsgStatus(testId, testEmail,1);
        System.out.println(testResult);
    }
}
