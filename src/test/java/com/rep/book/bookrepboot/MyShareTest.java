package com.rep.book.bookrepboot;

import com.rep.book.bookrepboot.dto.PageDTO;
import com.rep.book.bookrepboot.service.MyShareService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class MyShareTest {
    @Autowired
    MyShareService myShareService;

    @Test
    public void getReceivedTradeMsgTest(){
        String testEmail = "test01@naver.com";
        List<PageDTO> testList = myShareService.getReceivedTradeMsg(testEmail);
        System.out.println(testList);
    }

    @Test
    public void getSentTradeMsgTest(){
        String testEmail = "test01@naver.com";
        List<PageDTO> testList = myShareService.getSentTradeMsg(testEmail);
        System.out.println(testList);
    }

    @Test
    public void getRegisterListTest(){
        String testEmail = "xodlsdldy@naver.com";
        List<PageDTO> testList = myShareService.getRegisterList(testEmail);
        System.out.println(testList);
    }
}