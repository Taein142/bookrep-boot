package com.rep.book.bookrepboot.dao;

import com.rep.book.bookrepboot.dto.LikeDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LikeDao {

   Integer getLikeValueByReportId(Long id);

   void setLike(LikeDTO likeDTO);
   
   void removeLike(LikeDTO likeDTO);

	int isLike(LikeDTO likeDTO);

}