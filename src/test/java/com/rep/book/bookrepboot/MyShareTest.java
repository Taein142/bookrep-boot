package com.rep.book.bookrepboot;

import com.rep.book.bookrepboot.dto.MsgDTO;
import com.rep.book.bookrepboot.dto.PageDTO;
import com.rep.book.bookrepboot.service.MyShareService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.List;

@SpringBootTest
public class MyShareTest {
    @Autowired
    private MyShareService myShareService;

    @Test
    public void getReceivedTradeMsgTest(){
        String testEmail = "test02@naver.com";
        List<Object> testList = myShareService.getReceivedTradeMsg(testEmail);
        System.out.println(testList);
    }

    @Test
    public void getSentTradeMsgTest(){
        String testEmail = "test02@naver.com";
        List<Object> testList = myShareService.getSentTradeMsg(testEmail);
        System.out.println(testList);
    }

    @Test
    public void getRegisterListTest(){
        String testEmail = "xodlsdldy@naver.com";
        List<Object> testList = myShareService.getRegisterList(testEmail);
        System.out.println(testList);
    }

    @Test
    public void deleteTradeRegistrationTest(){
        Long testId = 22L;
        boolean result = myShareService.deleteTradeRegistration(testId);
        System.out.println(result);
    }
}
