package com.rep.book.bookrepboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rep.book.bookrepboot.dao.BookDao;
import com.rep.book.bookrepboot.dao.ReportDao;
import com.rep.book.bookrepboot.dto.BookDTO;
import com.rep.book.bookrepboot.dto.ReportDTO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookService {
	
	@Autowired
	private BookDao bookDao;
	
	@Autowired
	private ReportDao reportDao;

	public BookDTO getBookByIsbn(String isbn) {
		log.info("getBookByIsbn()");
		
		BookDTO bookDTO = bookDao.getBookByIsbn(isbn);
		
		return bookDTO;
	}
	
	public List<ReportDTO> getReportByIsbn(String isbn) {
		log.info("getReportByIsbn()");
		
		List<ReportDTO> reportList = reportDao.getReportByIsbn(isbn);
		
		return reportList;
	}

}
