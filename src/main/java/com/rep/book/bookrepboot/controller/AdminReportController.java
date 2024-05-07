package com.rep.book.bookrepboot.controller;

import com.rep.book.bookrepboot.dto.PageDTO;
import com.rep.book.bookrepboot.service.AdminReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Slf4j
public class AdminReportController {
    @Autowired
    private AdminReportService adminReportService;

    @GetMapping("admin/main-report-management")
    public String showReportManagement(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                       @RequestParam(value = "option", defaultValue = "title") String option,
                                       @RequestParam(value = "keyword", required = false) String keyword,
                                       Model model){
        log.info("showReportManagement()");
        List<PageDTO> reportList = adminReportService.getReportByKeyword(option, keyword);
        log.info("reportList{}", reportList);

        if (reportList.size()>0){
            model.addAttribute("reportList",reportList.get(pageNum-1));
        } else {
            model.addAttribute("reportList", null);
        }
        model.addAttribute("listMaxSize",reportList.size());
        model.addAttribute("currentPageNum", pageNum);
        model.addAttribute("keyword", keyword);
        model.addAttribute("option", option);

        return "th/reportManagement";
    }

    // 독후감 삭제
    @PostMapping("admin/report-delete")
    @ResponseBody
    public String deleteReport(@RequestParam(value = "reportId") String reportId){
        log.info("deleteReport()");
        Long id = Long.valueOf(reportId);
        try {
            boolean success = adminReportService.deleteReportById(id);
            if (success) {
                log.info("id=" + reportId + " 독후감이 삭제되었습니다.");
                return "ok";
            } else {
                log.error("id=" + reportId + " 독후감 삭제에 실패했습니다.");
                return "fail";
            }
        } catch (Exception e) {
            log.error("독후감 삭제 중 오류가 발생했습니다.", e);
            return "fail";
        }
    }
}
