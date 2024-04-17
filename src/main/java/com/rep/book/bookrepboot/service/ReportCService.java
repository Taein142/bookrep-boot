package com.rep.book.bookrepboot.service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import com.rep.book.bookrepboot.util.SecurityUtil;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rep.book.bookrepboot.APIKEY;
import com.rep.book.bookrepboot.dao.BookDao;
import com.rep.book.bookrepboot.dao.ReportDao;
import com.rep.book.bookrepboot.dto.BookDTO;
import com.rep.book.bookrepboot.dto.ReportDTO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReportCService {
	
	@Autowired
	private ReportDao reportDao;
	
	@Autowired
	private BookDao bookDao;

	public List<BookDTO> getBookByAPI(String keyword) throws IOException, InterruptedException {
		log.info("getBookByAPI()");
		
		HttpClient client = HttpClient.newHttpClient();
		String url = "https://openapi.naver.com/v1/search/book.json"
				+ "?query=" + URLEncoder.encode(keyword, "UTF-8")
				+ "&display=100";
		HttpRequest request = HttpRequest.newBuilder()
				.header("X-Naver-Client-Id", APIKEY.ID)
				.header("X-Naver-Client-Secret", APIKEY.SECRET)
				.uri(URI.create(url))
				.GET()
				.build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		String responseBody = response.body();
		NaverBook naverBook = new ObjectMapper().readValue(responseBody, NaverBook.class);
		List<NaverBook.Item> items = naverBook.getItems();
		List<BookDTO> bookList = new ArrayList<BookDTO>();
		try {
			for (NaverBook.Item item : items) {
				BookDTO bookDTO = new BookDTO(item.getTitle(), item.getAuthor(), item.getPublisher(), item.getIsbn(), item.getImage());
				bookList.add(bookDTO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bookList;
	}
	
	public void saveBook(BookDTO bookDTO) {
		log.info("saveBook()");
		BookDTO bookDTO2 = bookDao.getBookByIsbn(bookDTO.getIsbn());
		if (bookDTO2 == null) {
			bookDao.saveBook(bookDTO);
		}
	}
	
	public String setReport(HttpSession session, ReportDTO reportDTO, RedirectAttributes rttr) {
		log.info("setReport()");
		
		String msg = null;
		String view = null;
		String email = SecurityUtil.getCurrentUserEmail();
		if (email == null) {
			msg = "로그인 먼저 하세요";
			view = "redirect:sign-in";
		} else {
			try {
				reportDao.setReport(reportDTO);
				msg = "작성완료";
				view = "redirect:user/feed/" + email;
			} catch (Exception e) {
				e.printStackTrace();
				msg = "다시 입력해주세요";
				view = "redirect:user/write";
			}
		}
		
		rttr.addFlashAttribute(msg);
	
		return view;
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class NaverBook{
		private List<Item> items;
		
		
		public List<Item> getItems() {
			return items;
		}


		@JsonIgnoreProperties(ignoreUnknown = true)
		public static class Item{
			
			private String title;
			
			private String author;
			
			private String publisher;
			
			private String isbn;
			
			private String image;

			public String getTitle() {
				return title;
			}

			public String getAuthor() {
				return author;
			}

			public String getPublisher() {
				return publisher;
			}

			public String getIsbn() {
				return isbn;
			}

			public String getImage() {
				return image;
			}
			
		}
	}

	

}
