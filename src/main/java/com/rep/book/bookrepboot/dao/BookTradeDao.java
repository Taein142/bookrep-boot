package com.rep.book.bookrepboot.dao;

import com.rep.book.bookrepboot.dto.BookTradeDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BookTradeDao {
    void registerTrade(BookTradeDTO bookTradeDTO);

    List<BookTradeDTO> getBookTradeByKeyword(Map<String, String> map);

    BookTradeDTO getInfoById(Long id);

    List<BookTradeDTO> getBookTradeByEmail(String loggedInUserEmail);

    void deleteTradeRegistration(Long id);

    void updateBookTradeStatus(Long bookTradeId);
}
