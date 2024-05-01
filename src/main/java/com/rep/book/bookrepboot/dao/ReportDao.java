package com.rep.book.bookrepboot.dao;

import java.util.List;
import com.rep.book.bookrepboot.dto.ReportDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReportDao {
	
	void setReport(ReportDTO reportDTO);

	ReportDTO getReportDetailByReportId(Long id);

	void deleteReportByReportId(Long id);

	void applyReportUpdate(ReportDTO reportDTO);
  
	List<ReportDTO> getReportSummaryById(String userEmail);

	List<ReportDTO> getReportOfFollowing(String email);

	List<ReportDTO> getReportByIsbn(String isbn);

	List<ReportDTO> getReportByTitle(String keyword);

	List<ReportDTO> getReportByBookName(String keyword);

	List<ReportDTO> getReportByUserEmail(String keyword);
}
