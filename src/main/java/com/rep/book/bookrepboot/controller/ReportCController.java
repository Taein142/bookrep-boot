package com.rep.book.bookrepboot.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rep.book.bookrepboot.dto.BookDTO;
import com.rep.book.bookrepboot.dto.PageDTO;
import com.rep.book.bookrepboot.dto.ReportDTO;
import com.rep.book.bookrepboot.service.ReportCService;
import com.rep.book.bookrepboot.util.MainUtil;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ReportCController {
	
	@Autowired
	private ReportCService reportCService;
	
	@GetMapping("user/write")
	public String showReportWriter(@ModelAttribute("book") BookDTO bookDTO) {
		log.info("showReportWriter()");
		return "write";
	}
	
	@GetMapping("user/book-search")
	public String getBookSearch(@RequestParam("keyword") String keyword, Model model) throws IOException, InterruptedException {
		log.info("getBookSearch()");
		List<BookDTO> bookList = reportCService.getBookByAPI(keyword);
		List<PageDTO> bookPageList = MainUtil.setPaging(bookList, 6);
		model.addAttribute("bookList", bookPageList);
		model.addAttribute("keyword", keyword);
		return "bookSearch";
	}
	
	@GetMapping("user/apply")
	public String applyBookSearch(RedirectAttributes rttr, BookDTO bookDTO) {
		log.info("applyBookSearch()");
		reportCService.saveBook(bookDTO);

		rttr.addFlashAttribute("book", bookDTO);
		return "redirect:write";
	}
	
	@PostMapping("user/save")
	public String setReport(HttpSession session, ReportDTO reportDTO, RedirectAttributes rttr) {
		log.info("setReport()");
		
		String view = reportCService.setReport(session, reportDTO, rttr);
		
		return view;
	}
	
}
