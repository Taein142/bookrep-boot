package com.rep.book.bookrepboot.util;

import java.util.ArrayList;
import java.util.List;

import com.rep.book.bookrepboot.dto.BookDTO;
import com.rep.book.bookrepboot.dto.PageDTO;
import com.rep.book.bookrepboot.dto.UserDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainUtil {
	private static int maxNum=0;
	
	@SuppressWarnings("rawtypes")
	public static <T> List<PageDTO> setPaging(List<T> objectList, int maxCnt){
		log.info("setPaging()에 들어옴");
		if (objectList.size() % maxCnt == 0) {
			maxNum = objectList.size() / maxCnt;
		}else {
			maxNum = objectList.size() / maxCnt + 1;
		}
		
		List<PageDTO> pageDTOList = new ArrayList<>();
		
		for(int i=0; i<maxNum; i++) {
			List<T> onePageList = new ArrayList<T>();
			for (int j = maxCnt * i; j < maxCnt * (i + 1) && j < objectList.size(); j++) {
				onePageList.add(objectList.get(j));
			}
			PageDTO<T> pageDTO = new PageDTO<T>(i + 1, onePageList);
			pageDTOList.add(pageDTO);
		}
		return pageDTOList;
	}

	public static List<UserDTO> setPagingUser(List<UserDTO> userList, Integer userPageNum) {
		log.info("setPagingUser()");
		
		int from = 6 * (userPageNum - 1);
		int end;
		if (from + 5 > userList.size() - 1) {
			end = userList.size();
		}else {
			end = from + 6;
		}
		List<UserDTO> currentUserList = userList.subList(from, end);
		return currentUserList;
	}

	public static List<BookDTO> setPagingBook(List<BookDTO> bookList, Integer bookPageNum) {
		log.info("setPagingBook()");
		
		int from = 6 * (bookPageNum - 1);
		int end;
		if (from + 5 > bookList.size() - 1) {
			end = bookList.size();
		}else {
			end = from + 6;
		}
		List<BookDTO> currentBookList = bookList.subList(from, end);
		return currentBookList;
	}

	public static List<String> setPagingFollow(List<String> followingList, Integer currentPage) {
		log.info("setPagingFollow()");
		
		int from = 6 * (currentPage - 1);
		int end;
		if (from + 5 > followingList.size() - 1) {
			end = followingList.size();
		}else {
			end = from + 6;
		}
		List<String> currentFollowingList = followingList.subList(from, end);
		
		return currentFollowingList;
	}
}

