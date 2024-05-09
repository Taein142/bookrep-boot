package com.rep.book.bookrepboot.dao;


import com.rep.book.bookrepboot.dto.NodeCostDTO;
import com.rep.book.bookrepboot.dto.PathDTO;
import com.rep.book.bookrepboot.dto.TradeDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TradeDao {

    void setTrade(TradeDTO tradeDTO);

    List<TradeDTO> getTradeListByEmail(String email);

    Long getLastId();

    List<TradeDTO> getTradeRemainder();

    NodeCostDTO getOneByParam(NodeCostDTO nodeCostDTO);

    void add(NodeCostDTO nodeCost);

    void updateTrade(TradeDTO tradeDTO);

    void completeTrade(TradeDTO tradeDTO);
}