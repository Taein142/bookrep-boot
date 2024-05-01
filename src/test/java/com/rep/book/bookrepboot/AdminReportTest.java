package com.rep.book.bookrepboot;

import com.rep.book.bookrepboot.dto.PageDTO;
import com.rep.book.bookrepboot.service.AdminReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class AdminReportTest {
    @Autowired
    private AdminReportService adminReportService;

    @Test
    public void getReportByKeywordTest(){
        String testKeyword = "";
        String testOption = "제목";
        List<PageDTO> testList = adminReportService.getReportByKeyword(testOption,testKeyword);
        System.out.println(testList);

    }
}
