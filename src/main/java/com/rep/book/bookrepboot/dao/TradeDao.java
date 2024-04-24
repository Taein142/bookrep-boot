package com.rep.book.bookrepboot.dao;


import com.rep.book.bookrepboot.dto.TradeDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TradeDao {

    void setTrade(TradeDTO tradeDTO);
}