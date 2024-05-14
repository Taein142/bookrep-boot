package com.rep.book.bookrepboot.service;

import com.rep.book.bookrepboot.dao.CommentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rep.book.bookrepboot.dao.ReportDao;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReportDService {
	
	@Autowired
	private ReportDao reportDao;
	@Autowired
	private CommentDao commentDao;

	public void deleteReportByReportId(Long id) {
		log.info("deleteReportByReportId()");
		
		reportDao.deleteReportByReportId(id);
		
	}

    public boolean deleteComment(Long id) {
		boolean result = false;
		try {
			commentDao.deleteCommentById(id);
			result = true;
		} catch (Exception e){
			e.printStackTrace();
			result = false;
			log.info("result{}", result);
		}
		return result;
    }
}
