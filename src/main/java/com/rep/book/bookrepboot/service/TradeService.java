package com.rep.book.bookrepboot.service;


import com.rep.book.bookrepboot.dao.TradeDao;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TradeService {
    @Autowired
    TradeDao tradeDao;

}
