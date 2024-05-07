package com.rep.book.bookrepboot.service;

import com.rep.book.bookrepboot.dao.BookDao;
import com.rep.book.bookrepboot.dao.ReportDao;
import com.rep.book.bookrepboot.dto.BookDTO;
import com.rep.book.bookrepboot.dto.PageDTO;
import com.rep.book.bookrepboot.dto.ReportDTO;
import com.rep.book.bookrepboot.util.MainUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AdminReportService {
    @Autowired
    ReportDao reportDao;
    @Autowired
    BookDao bookDao;

    public List<PageDTO> getReportByKeyword(String option, String keyword) {
        log.info("getReportByKeyword()");
        List<ReportDTO> reportList = new ArrayList<>();
        if (option == null || option.isEmpty()){

        } else {
            if (option.equals("user")) {
                reportList = reportDao.getReportByUserEmail(keyword);
            } else if (option.equals("book")) {
                reportList = reportDao.getReportByBookName(keyword);
            } else {
                reportList = reportDao.getReportByTitle(keyword);
            }
        }
        List<Object> mappingList = new ArrayList<>();
        for (int i = 0; i < reportList.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("report", reportList.get(i));
            String bookName = bookDao.getBookNameByIsbn(reportList.get(i).getBookIsbn());
            map.put("bookName", bookName);
            mappingList.add(map);
        }

        return MainUtil.setPaging(mappingList, 10);
    }

    public boolean deleteReportById(Long reportId) {
        log.info("deleteReportById()");
        boolean result = false;
        try{
            reportDao.deleteReportByReportId(reportId);
            log.info("report_id" + reportId + "인 독후감이 삭제되었습니다.");
            return true;
        } catch (Exception e){
            e.printStackTrace();
            result = false;
        }
        return result;
    }
}
