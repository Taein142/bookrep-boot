package com.rep.book.bookrepboot;

import com.rep.book.bookrepboot.dto.PageDTO;
import com.rep.book.bookrepboot.service.AdminBookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class AdminBookTest {
    @Autowired
    private AdminBookService adminBookService;

    @Test
    public void getBookByKeywordTest(){
        String testOption = "name";
        String testKeyword = "자바";
        List<PageDTO> testList = adminBookService.getBookByKeyword(testOption,testKeyword);
        System.out.println(testList);
    }
}
