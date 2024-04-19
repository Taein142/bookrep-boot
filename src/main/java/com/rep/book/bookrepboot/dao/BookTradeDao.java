package com.rep.book.bookrepboot.dao;

import com.rep.book.bookrepboot.dto.BookTradeDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookTradeDao {
    void registerTrade(BookTradeDTO bookTradeDTO);

    List<BookTradeDTO> getBookTradeByKeyword(String keyword);
}
