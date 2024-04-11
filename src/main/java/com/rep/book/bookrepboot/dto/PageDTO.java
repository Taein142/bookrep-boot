package com.rep.book.bookrepboot.dto;

import java.util.List;

import lombok.Data;
import lombok.NonNull;

@Data
public class PageDTO<T> {
	@NonNull
	private int pageNum;
	
	@NonNull
	private List<T> objectList;

}
