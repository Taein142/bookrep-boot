package com.rep.book.bookrepboot.dao;

import com.rep.book.bookrepboot.dto.MsgDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TradeMsgDao {
    void setTradeMsg(MsgDTO msgDTO);
}
