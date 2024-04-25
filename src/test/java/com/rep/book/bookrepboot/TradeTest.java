package com.rep.book.bookrepboot;

import com.rep.book.bookrepboot.dto.PageDTO;
import com.rep.book.bookrepboot.service.TradeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
}
