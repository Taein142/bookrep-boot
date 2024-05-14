package com.rep.book.bookrepboot.controller;

import java.util.List;

import com.rep.book.bookrepboot.util.SecurityUtil;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rep.book.bookrepboot.dto.CommentDTO;
import com.rep.book.bookrepboot.dto.PageDTO;
import com.rep.book.bookrepboot.dto.ReportDTO;
import com.rep.book.bookrepboot.service.ReportRService;
import com.rep.book.bookrepboot.util.MainUtil;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ReportRController {
	
	@Autowired
	private ReportRService reportRService; 
	
	@GetMapping("user/report-detail")
	public String showReportDetail(@RequestParam("id") Long id, Model model) {
		log.info("showReportDetail()");
		
		String loggedInUserEmail = SecurityUtil.getCurrentUserEmail();
		
		ReportDTO reportDTO = reportRService.getReportDetailByReportId(id);
		List<CommentDTO> commentList = reportRService.getCommentByReportId(id);
		List<PageDTO> pageList = MainUtil.setPaging(commentList, 5);
		Integer likeValue = reportRService.getLikeValueByReportId(id);
		Integer isLike = reportRService.isLike(loggedInUserEmail, id);
		
		boolean isLikeBool = false;
		if (isLike == 1) {
			isLikeBool = true;
		}
		
		model.addAttribute("report", reportDTO);
		model.addAttribute("commentList", pageList);
		model.addAttribute("likeValue", likeValue);
		model.addAttribute("isLike", isLikeBool);
		model.addAttribute("loggedInUserEmail", loggedInUserEmail);
		
		
		return "reportDetail";
	}
	
	@PostMapping("user/like")
	public String setLike(@RequestParam("id") Long id, HttpSession session) {
		log.info("setLike()");
		
		String email = SecurityUtil.getCurrentUserEmail();
		
		reportRService.setLike(email, id);
		
		return "redirect:report-detail?id=" + id;
	}
	
	@PostMapping("user/comment")
	public String setComment(@RequestParam("id") Long id, HttpSession session, @RequestParam("comment") String comment) {
		log.info("setComment()");
		
		String email = SecurityUtil.getCurrentUserEmail();
		
		reportRService.setComment(email, id, comment);
		
		return "redirect:report-detail?id=" + id;
	}
	
}
