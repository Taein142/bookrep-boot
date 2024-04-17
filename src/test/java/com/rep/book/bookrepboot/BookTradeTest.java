package com.rep.book.bookrepboot;

import com.rep.book.bookrepboot.dto.BookTradeDTO;
import com.rep.book.bookrepboot.service.BookTradeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

@SpringBootTest
public class BookTradeTest {
    @Autowired
    BookTradeService bookTradeService;

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
}