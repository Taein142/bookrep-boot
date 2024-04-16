package com.rep.book.bookrepboot.service;

import com.rep.book.bookrepboot.dao.BookDao;
import com.rep.book.bookrepboot.dto.BookDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TradeService {
    @Autowired
    BookDao bookDao;

    public List<BookDTO> getBookByDB(String loggedInUserEmail, String keyword) {
        List<BookDTO> bookList = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("email", loggedInUserEmail);
        map.put("keyword", keyword);

        try {
            bookList = bookDao.getOwnBookByKeyword(map);
        } catch (Exception e){
            e.printStackTrace();
        }

        return bookList;
    }
}
