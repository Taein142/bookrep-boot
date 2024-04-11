package com.rep.book.bookrepboot.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
	
	private Long id;
	
	@NonNull
	private String userEmail;
	
	@NonNull
	private Long reportId;
	
	@NonNull
	private String content;
	
	private LocalDateTime time;
	
}
