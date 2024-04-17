package com.rep.book.bookrepboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.rep.book.bookrepboot.dto.BookDTO;
import com.rep.book.bookrepboot.dto.PageDTO;
import com.rep.book.bookrepboot.service.BookmarkService;
import com.rep.book.bookrepboot.util.MainUtil;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class BookmarkController {
	
	@Autowired
	private BookmarkService bookmarkService;
	
	@GetMapping("user/bookmark/{email}")
	public String showBookmark(@PathVariable String email,
								Model model,
								@RequestParam(required = false) Integer pageNum) {
		log.info("showBookmark()");
		
		List<BookDTO> bookList = bookmarkService.getBookmarkByEmail(email);
		Integer currentPageNum = 1;
		if (pageNum != null) {
			currentPageNum = pageNum;
		}
		List<BookDTO> currentBookList = MainUtil.setPagingBook(bookList, currentPageNum);
		model.addAttribute("bookmarkList", currentBookList);
		model.addAttribute("currentPageNum", currentPageNum);
		model.addAttribute("email", email);
		model.addAttribute("totalBookSize", bookList.size());
		return "bookmark";
	}
}
