package com.rep.book.bookrepboot.util.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoAddress {
	private List<Document> documents;

	public List<Document> getDocuments() {
		return documents;
	}

}