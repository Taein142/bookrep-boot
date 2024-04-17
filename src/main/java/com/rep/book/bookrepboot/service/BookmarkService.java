package com.rep.book.bookrepboot.service;

import java.util.List;

import com.rep.book.bookrepboot.util.SecurityUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rep.book.bookrepboot.dao.BookDao;
import com.rep.book.bookrepboot.dao.BookmarkDao;
import com.rep.book.bookrepboot.dto.BookDTO;
import com.rep.book.bookrepboot.dto.BookmarkDTO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookmarkService {
	
	@Autowired
	private BookmarkDao bookmarkDao;
	
	@Autowired
	private BookDao bookDao;

	public boolean isBookmark(HttpSession session, String isbn) {
		log.info("isBookmark()");
		
		String email = SecurityUtil.getCurrentUserEmail();

        assert email != null;
        BookmarkDTO bookmarkDTO = new BookmarkDTO(email, isbn);
		
		int isBookmark = bookmarkDao.isBookmark(bookmarkDTO);
		
		boolean isBookmarkBool = false;
		if (isBookmark != 0) {
			isBookmarkBool = true;
		}
		
		return isBookmarkBool;
	}


	public void setBookmark(HttpSession session, String isbn) {
		log.info("setBookmark()");
		
		String email = SecurityUtil.getCurrentUserEmail();
        assert email != null;
        BookmarkDTO bookmarkDTO = new BookmarkDTO(email, isbn);
		
		bookmarkDao.setBookmark(bookmarkDTO);
		
	}


	public List<BookDTO> getBookmarkByEmail(String email) {
		log.info("getBookmarkByEmail");
		
		List<BookDTO> bookmarkList = bookDao.getBookmarkByEmail(email);
		
		return bookmarkList;
	}


	public void removeBookmark(HttpSession session, String isbn) {
		log.info("removeBookmark()");
		
		String email = SecurityUtil.getCurrentUserEmail();
        assert email != null;
        BookmarkDTO bookmarkDTO = new BookmarkDTO(email, isbn);
		
		bookmarkDao.removeBookmark(bookmarkDTO);
	}

}
