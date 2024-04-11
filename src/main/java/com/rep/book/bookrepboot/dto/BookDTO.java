package com.rep.book.bookrepboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

	
	private String name;
	
	
	private String author;
	
	
	private String publisher;
	
	
	private String isbn;
	
	
	private String image;
	
}
