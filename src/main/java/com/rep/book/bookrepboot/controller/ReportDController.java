package com.rep.book.bookrepboot.controller;

import com.rep.book.bookrepboot.util.SecurityUtil;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rep.book.bookrepboot.service.ReportDService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ReportDController {
	
	@Autowired
	private ReportDService reportDService;
	
	@GetMapping("user/delete")
	public String deleteReportByReportId(@RequestParam("id") Long id, HttpSession session, RedirectAttributes rttr , @RequestParam("reportUserEmail") String reportEmail) {
		log.info("deleteReportByReportId()");
		
		String email = SecurityUtil.getCurrentUserEmail();
		String msg = null;
		String view = null;
		if (email == null || !email.equals(reportEmail)) {
			msg = "you are not an owner of this report";
			view = "redirect:report-detail?id=" + id;
		}else {
			try {
				reportDService.deleteReportByReportId(id);
				msg = "delete complete";
				view = "redirect:/";
			} catch (Exception e) {
				e.printStackTrace();
				msg = "delete failed";
				view = "redirect:report-detail?id=" + id;
			}
		}
		rttr.addFlashAttribute("msg", msg);
		return view;
	}

	@PostMapping("user/comment-delete")
	@ResponseBody
	public String deleteCommentById(@RequestParam(value = "commentId") Long id){
		log.info("deleteComment()");
		try{
			boolean success = reportDService.deleteComment(id);
			log.info(String.valueOf(success));
			if (success){
				log.info("id= " + id + "댓글이 삭제되었습니다.");
				return "ok";
			} else {
				log.info("id= " + id + "댓글이 삭제에 실패했다.");
				return "fail";
			}
		} catch (Exception e) {
			log.error("댓글 삭제 중 오류가 발생했습니다.", e);
			return "fail";
		}
	}
}
