package com.rep.book.bookrepboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
	
	@NonNull
	private String email;
	
	@NonNull
	private String password;
	
	@NonNull
	private String name;
	
	
	private String image;

	private Double longitude;

	private Double latitude;

	private String detail;

	private final String role = "USER";
	
}
