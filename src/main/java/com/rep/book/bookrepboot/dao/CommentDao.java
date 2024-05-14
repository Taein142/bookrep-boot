package com.rep.book.bookrepboot.dao;

import java.util.List;
import java.util.Map;

import com.rep.book.bookrepboot.dto.CommentDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentDao {

	List<CommentDTO> getCommentByReportId(Long id);

	void setComment(CommentDTO commentDTO);

    void updateComment(Map<String, Object> map);

	void deleteCommentById(Long id);
}
