package com.rep.book.bookrepboot.util;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
@Setter
@Getter
public class JsonResult { // db에서 데이터를 가져와서 json형태로 바꿔서 서버로 보내줄 때 사용
 public enum Code { // enum: 열거형, 일련의 상수들을 정의할 때 사용
   SUCC, FAIL
 }
 private Code code = Code.SUCC;
 private String msg;
 private Map<String, Object> data;
 public void addData(String key, Object value) {
   if (data == null) {
     data = new HashMap<>();
   }
   data.put(key, value);
 }
}
