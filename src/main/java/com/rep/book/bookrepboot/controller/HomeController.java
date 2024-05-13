package com.rep.book.bookrepboot.controller;

import java.util.List;

import com.rep.book.bookrepboot.util.SecurityUtil;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rep.book.bookrepboot.dto.PageDTO;
import com.rep.book.bookrepboot.service.FeedService;
import com.rep.book.bookrepboot.service.HomeService;


import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class HomeController {


	@Autowired
	private HomeService homeService;
	
	@Autowired
	private FeedService feedService;
	
	@GetMapping("/")
	public String index() {
		return "home1";
	}
	
	@GetMapping("/user/home")
	public String home(HttpSession session, Model model) {
    
		String email = SecurityUtil.getCurrentUserEmail();
    
		log.info(email);
		
		if (email != null) {
			log.info("home2");

			List<PageDTO> pageList = homeService.getReportToHome(email);
			
			model.addAttribute("sessionItems", pageList);
			return "home2";
		} else {
			log.info("home1");
			return "redirect:/";
		}
	}
	
	@PostMapping("user/get-image")
	@ResponseBody
	public String getImage(@RequestParam("email") String email) {
		log.info("getImage()");
		String imageAjax = feedService.getUserImage(email);
		return imageAjax;
	}

	@GetMapping("privacy-policy")
	public String showPrivacyPolicy(){
		log.info("showPrivacyPolicy()");
		return "th/privacyPolicy";
	}

	@GetMapping("terms")
	public String showTerms(){
		log.info("showTerms");
		return "th/terms";
	}

	@GetMapping("contact-us")
	public String showContactUs(){ return "th/contactUs"; }
}

