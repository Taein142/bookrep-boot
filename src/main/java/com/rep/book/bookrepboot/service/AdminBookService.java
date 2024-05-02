package com.rep.book.bookrepboot.service;

import com.rep.book.bookrepboot.dao.BookDao;
import com.rep.book.bookrepboot.dto.BookDTO;
import com.rep.book.bookrepboot.dto.PageDTO;
import com.rep.book.bookrepboot.util.MainUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AdminBookService {
    @Autowired
    private BookDao bookDao;

    public List<PageDTO> getBookByKeyword(String option, String keyword) {
        log.info("getBookByKeyword");
        List<BookDTO> bookList = new ArrayList<>();

        if (option.equals("author")) {
            bookList = bookDao.getBookByAuthor(keyword);
        } else if (option.equals("publisher")) {
            bookList = bookDao.getBookByPublisher(keyword);
        } else {
            bookList = bookDao.getBookByName(keyword);
        }
        return MainUtil.setPaging(bookList, 10);
    }
}
