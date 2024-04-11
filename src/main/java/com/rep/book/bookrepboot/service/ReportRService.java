package com.rep.book.bookrepboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rep.book.bookrepboot.dao.CommentDao;
import com.rep.book.bookrepboot.dao.LikeDao;
import com.rep.book.bookrepboot.dao.ReportDao;
import com.rep.book.bookrepboot.dto.CommentDTO;
import com.rep.book.bookrepboot.dto.LikeDTO;
import com.rep.book.bookrepboot.dto.ReportDTO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReportRService {
   
   @Autowired
   private ReportDao reportDao;
   
   @Autowired
   private LikeDao likeDao;
   
   @Autowired
   private CommentDao commentDao;
   

   public ReportDTO getReportDetailByReportId(Long id) {
      log.info("getReportDetailByReportId()");
      
      ReportDTO reportDTO = reportDao.getReportDetailByReportId(id);
      
      return reportDTO;
   }

   public List<CommentDTO> getCommentByReportId(Long id) {
      log.info("getCommentByReportId()");
      
      List<CommentDTO> commentList = commentDao.getCommentByReportId(id);
      
      return commentList;
   }

   public Integer getLikeValueByReportId(Long id) {
      log.info("getLikeValueByReportId()");
      
      Integer likeValue = likeDao.getLikeValueByReportId(id);
      
      return likeValue;
   }

   public void setLike(String email, Long id) {
      log.info("setLike()");
      
      LikeDTO likeDTO = new LikeDTO(email, id);
      
      int likeValue = likeDao.isLike(likeDTO);
      
       // 좋아요 토글
       if (likeValue > 0) {
           likeDao.removeLike(likeDTO);
       } else {
           likeDao.setLike(likeDTO);
       }
   }

   public void setComment(String email, Long id, String content) {
      log.info("setComment()");
      
      CommentDTO commentDTO = new CommentDTO();
      commentDTO.setReportId(id);
      commentDTO.setContent(content);
      commentDTO.setUserEmail(email);
      commentDao.setComment(commentDTO);
   }
   
   public Integer isLike(String email, Long id) {
	   
	   LikeDTO likeDTO = new LikeDTO(email, id);
	   
	   int isLike = likeDao.isLike(likeDTO);
	   
	   return isLike;
   }

}