package com.rep.book.bookrepboot.dao;

import com.rep.book.bookrepboot.dto.BookTradeDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TradeDao {
    void registerTrade(BookTradeDTO bookTradeDTO);
}
