package com.rep.book.bookrepboot.service;


import com.rep.book.bookrepboot.dao.BookDao;
import com.rep.book.bookrepboot.dao.TradeDao;
import com.rep.book.bookrepboot.dto.BookDTO;
import com.rep.book.bookrepboot.dto.PageDTO;
import com.rep.book.bookrepboot.dto.TradeDTO;
import com.rep.book.bookrepboot.util.MainUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TradeService {
    @Autowired
    TradeDao tradeDao;
    @Autowired
    BookDao bookDao;

    public List<PageDTO> getTradeListByEmail(String email) {
        log.info("getTradeListByEmail()");
        List<TradeDTO> tradeDTOList = tradeDao.getTradeListByEmail(email);
        List<Object> tradeList = new ArrayList<>();
        for (int i = 0; i < tradeDTOList.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("tradeInfo", tradeDTOList.get(i));
            BookDTO firstBook = bookDao.getBookByIsbn(tradeDTOList.get(i).getFir_book_isbn());
            BookDTO secondBook = bookDao.getBookByIsbn(tradeDTOList.get(i).getSec_book_isbn());
            map.put("firstBook",firstBook);
            map.put("secondBook",secondBook);
            tradeList.add(map);
        }
        log.info("tradeList {}", tradeList);
        return MainUtil.setPaging(tradeList, 10);
    }
}
