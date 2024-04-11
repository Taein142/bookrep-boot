package com.rep.book.bookrepboot.dao;

import java.util.List;

import com.rep.book.bookrepboot.dto.CommentDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentDao {

	List<CommentDTO> getCommentByReportId(Long id);

	void setComment(CommentDTO commentDTO);

}
