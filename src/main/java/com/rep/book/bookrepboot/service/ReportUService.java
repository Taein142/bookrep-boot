package com.rep.book.bookrepboot.service;

import com.rep.book.bookrepboot.util.SecurityUtil;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.rep.book.bookrepboot.dao.ReportDao;
import com.rep.book.bookrepboot.dto.ReportDTO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReportUService {
	
	@Autowired
	private ReportDao reportDao;

	public String isOwner(HttpSession session, ReportDTO reportDTO, Model model) {
		log.info("isOwner()");
		
		String view = null;
		String email = SecurityUtil.getCurrentUserEmail();
		if (!email.equals(reportDTO.getUserEmail())) {
			view = "redirect:report-detail?id=" + reportDTO.getId();
		}else {
			model.addAttribute("report", reportDTO);
			view = "reportUpdate";
		}
		
		return view;
	}

	public void applyReportUpdate(ReportDTO reportDTO) {
		log.info("applyReportUpdate()");
		
		reportDao.applyReportUpdate(reportDTO);
	}

}
