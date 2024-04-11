package com.rep.book.bookrepboot.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
	
	private Long id;
	
	@NonNull
	private String userEmail;
	
	@NonNull
	private String bookIsbn;
	
	@NonNull
	private String title;
	
	@NonNull
	private String content;
	
	private boolean publicBool;
	
	private LocalDateTime time;
	

}
