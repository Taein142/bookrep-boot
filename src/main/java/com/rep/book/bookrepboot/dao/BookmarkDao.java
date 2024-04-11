package com.rep.book.bookrepboot.dao;

import com.rep.book.bookrepboot.dto.BookmarkDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookmarkDao {

	int isBookmark(BookmarkDTO bookmarkDTO);

	void setBookmark(BookmarkDTO bookmarkDTO);

	void removeBookmark(BookmarkDTO bookmarkDTO);


}
