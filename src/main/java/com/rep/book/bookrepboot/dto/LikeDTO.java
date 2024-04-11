package com.rep.book.bookrepboot.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class LikeDTO {
	
	@NonNull
	private String userEmail;
	
	@NonNull
	private Long reportId;
	
}
