package com.rep.book.bookrepboot.util.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Document {
  private String id;
	private Double x;
	private Double y;
	@JsonProperty("place_name")
	private String placeName;
	private String phone;
	@JsonProperty("road_address_name")
	private String roadAddressName;
}