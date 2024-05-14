package com.rep.book.bookrepboot.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rep.book.bookrepboot.dto.ReportDTO;
import com.rep.book.bookrepboot.service.ReportRService;
import com.rep.book.bookrepboot.service.ReportUService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ReportUController {
	
	@Autowired
	private ReportRService reportRService;
	
	@Autowired
	private ReportUService reportUService;
	
	@GetMapping("user/report-update")
	public String showReportUpdate(HttpSession session, @RequestParam("id") Long id, Model model) {
		log.info("showReportUpdate()");
		
		ReportDTO reportDTO = reportRService.getReportDetailByReportId(id);
		String view = reportUService.isOwner(session, reportDTO, model);
		
		return view;
	}
	
	@PostMapping("/user/apply-update")
	public String applyReportUpdate(@ModelAttribute ReportDTO reportDTO) {
	    log.info("applyReportUpdate()");
	    
	    reportUService.applyReportUpdate(reportDTO);
	    
	    return "redirect:/user/report-detail?id=" + reportDTO.getId();
	}

	@PostMapping("user/comment-update")
	public String updateComment(@RequestParam(value = "id") Long id,
								@RequestParam("comment") String comment,
								@RequestParam(value = "report_id") Long report_id){

		reportUService.updateComment(id, comment);

		return "redirect:/user/report-detail?id=" + report_id;
	}
	
	
	
	
}
