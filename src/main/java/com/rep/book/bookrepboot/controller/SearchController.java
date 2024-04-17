package com.rep.book.bookrepboot.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rep.book.bookrepboot.dto.BookDTO;
import com.rep.book.bookrepboot.dto.PageDTO;
import com.rep.book.bookrepboot.dto.UserDTO;
import com.rep.book.bookrepboot.service.SearchService;
import com.rep.book.bookrepboot.util.MainUtil;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class SearchController {
	
	@Autowired
	private SearchService searchService;
	
	@GetMapping("user/search")
	public String getTotalSearchByString(@RequestParam("keyword") String keyword,
											Model model,
											@RequestParam(required = false) Integer userPageNum,
											@RequestParam(required = false) Integer bookPageNum) throws JsonProcessingException {
		log.info("controller.getTotalSearchByString()");
		
		List<UserDTO> userList = searchService.getUserByString(keyword);
		List<BookDTO> bookList = searchService.getBookByString(keyword);
		for (int i = 0; i < userList.size(); i++) {
			log.info(userList.get(i).getName());
		}
		for (int i = 0; i < bookList.size(); i++) {
			log.info(bookList.get(i).getName());
		}
		List<UserDTO> currentUserList;
		List<BookDTO> currentBookList;
		
		if (userPageNum == null) {
			currentUserList = MainUtil.setPagingUser(userList, 1);
			model.addAttribute("currentUserPageNum", 1);
		}else {
			currentUserList = MainUtil.setPagingUser(userList, userPageNum);
			model.addAttribute("currentUserPageNum", userPageNum);
		}
		if (bookPageNum == null) {
			currentBookList = MainUtil.setPagingBook(bookList, 1);
			model.addAttribute("currentBookPageNum", 1);
		}else {
			currentBookList = MainUtil.setPagingBook(bookList, bookPageNum);
			model.addAttribute("currentBookPageNum", bookPageNum);
		}
		
		model.addAttribute("userList", currentUserList);
		model.addAttribute("bookList", currentBookList);
		model.addAttribute("keyword", keyword);
		model.addAttribute("totalUserSize", userList.size());
		model.addAttribute("totalBookSize", bookList.size());
		return "search";
	}
	
	@PostMapping("user/user-result")
	@ResponseBody
	public String getUserResult(@RequestParam("keyword") String keyword, @RequestParam("pageNum") int pageNum) throws JsonProcessingException {
		log.info("getUserResult()");
		
		List<UserDTO> userList = searchService.getUserByString(keyword);
		
		
		String userListJson = new ObjectMapper().writeValueAsString(userList);
		
		return userListJson;
	}
	
	@PostMapping("user/book-result")
	@ResponseBody
	public String getBookResult(@RequestParam("keyword") String keyword, @RequestParam("pageNum") int pageNum) throws JsonProcessingException {
		log.info("getBookResult()");
		
		List<BookDTO> bookList = searchService.getBookByString(keyword);
		String bookListJson = new ObjectMapper().writeValueAsString(bookList);
		
		return bookListJson;
	}
}
