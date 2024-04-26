package com.rep.book.bookrepboot;

import com.rep.book.bookrepboot.dto.BookDTO;
import com.rep.book.bookrepboot.dto.BookTradeDTO;
import com.rep.book.bookrepboot.dto.PageDTO;
import com.rep.book.bookrepboot.service.BookTradeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.List;

@SpringBootTest
public class BookTradeTest {
    @Autowired
    private BookTradeService bookTradeService;

    @Test
    public void saveTradeResisterTest() {
        BookTradeDTO bookTradeDTO = new BookTradeDTO();
        bookTradeDTO.setUser_email("xodlsdldy@naver.com");
        bookTradeDTO.setBook_isbn("9791169212298");
        bookTradeDTO.setBook_quantity(1);

        RedirectAttributes rttr = new RedirectAttributesModelMap();
        String view = bookTradeService.saveTradeRegister(bookTradeDTO, rttr);
        System.out.println(view);
    }

    @Test
    public void getOwnBookByDBTest(){
        String testEmail = "xodlsdldy@naver.com";
        String keyword = null;
        List<BookDTO> bookList = bookTradeService.getUnTradeBookByDB(testEmail, keyword);

        System.out.println(bookList);
    }

    @Test
    public void getTradeListByKeywordTest(){
        String keyword = "자바";
        List<PageDTO> getTradeListByKeyword = bookTradeService.getTradeListByKeyword(keyword);
        System.out.println(getTradeListByKeyword);
    }

    @Test
    public void getInfoByIDTest(){
        Long testId = 1L;
        BookTradeDTO testInfo = bookTradeService.getInfoById(testId);
        System.out.println(testInfo);
    }
}
