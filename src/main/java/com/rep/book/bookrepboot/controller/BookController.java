package com.rep.book.bookrepboot.controller;

import java.util.List;


import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rep.book.bookrepboot.dto.BookDTO;
import com.rep.book.bookrepboot.dto.PageDTO;
import com.rep.book.bookrepboot.dto.ReportDTO;
import com.rep.book.bookrepboot.service.BookService;
import com.rep.book.bookrepboot.service.BookmarkService;
import com.rep.book.bookrepboot.util.MainUtil;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class BookController {
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private BookmarkService bookmarkService;
	
	
	@GetMapping("user/book-detail")
	public String showBookDetail(HttpSession session, @RequestParam("isbn") String isbn, Model model) {
		log.info("showBookDetail()");
		
		boolean isBookmark = bookmarkService.isBookmark(session, isbn);
		
		BookDTO bookDTO = bookService.getBookByIsbn(isbn);
		
		List<ReportDTO> reportList = bookService.getReportByIsbn(isbn);
		List<PageDTO> reportPageList = MainUtil.setPaging(reportList, 6);
		
		model.addAttribute("reportList", reportPageList);
		model.addAttribute("isBookmark", isBookmark);
		model.addAttribute("book", bookDTO);
		
		return "bookDetail";
	}
	
	@PostMapping("user/bookmark")
	@ResponseBody
	public int setBookmark(HttpSession session, @RequestParam("isbn") String isbn) {
		log.info("setBookmark()");
		
		boolean bookmarkBool = bookmarkService.isBookmark(session, isbn);
		if (bookmarkBool) {
			bookmarkService.removeBookmark(session, isbn);
			return 0;
		}else {
			bookmarkService.setBookmark(session, isbn);
			return 1;
		}
	}
	
}
