package com.rep.book.bookrepboot.dao;

import com.rep.book.bookrepboot.dto.MsgDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TradeMsgDao {
    void setTradeMsg(MsgDTO msgDTO);

    List<MsgDTO> getReceivedTradeMsg(String loggedInUserEmail);

    List<MsgDTO> getSentTradeMsg(String loggedInUserEmail);

    MsgDTO getMsgById(Long msgId);

    boolean updateMsgStatus(Map<String, Object> map);

    List<Long> getTradingID();

    List<MsgDTO> getInProgressMsg(String loggedInUserEmail);
}
