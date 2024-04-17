package com.rep.book.bookrepboot.controller;

import java.util.List;

import com.rep.book.bookrepboot.util.SecurityUtil;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.rep.book.bookrepboot.dto.PageDTO;
import com.rep.book.bookrepboot.service.FeedService;
import com.rep.book.bookrepboot.service.FollowService;

import lombok.extern.slf4j.Slf4j;


@Controller
@Slf4j
public class FeedController {
	@Autowired
	private FeedService feedService;
	
	@Autowired
	private FollowService followService;

	@GetMapping("user/feed/{userEmail}")
	public String showFeed(@PathVariable String userEmail, HttpSession session, Model model) {

		// 세션에서 현재 로그인한 사용자 이메일 가져옴.
		String loggedInUserEmail = SecurityUtil.getCurrentUserEmail();
		
		boolean isFollowing = followService.isFollowing(userEmail, loggedInUserEmail);
		
		model.addAttribute("isFollowing", isFollowing);
		
		String name = feedService.getNameByEmail(userEmail);
		
		model.addAttribute("name", name);
		
		// 매개변수인 userEmail을 사용해서 해당 유저의 독후감 정보를 가져옴.
		List<PageDTO> sessionItems = feedService.getReportSummaryById(userEmail, loggedInUserEmail);

		// 페이지 주인인 유저의 정보를 전달
		model.addAttribute("userEmail", userEmail);
		
		// 페이지 유저의 사진 정보 가져오는 메서드
		String userImage = feedService.getUserImage(userEmail);
		
		// 유저 이미지 전달
		model.addAttribute("userImage", userImage);

		// 독후감 정보 추가
		model.addAttribute("sessionItems", sessionItems);

		// 로그인한 사용자와 현재 보고 있는 페이지의 주인이 같은가 판별
		if (loggedInUserEmail != null && loggedInUserEmail.equals(userEmail)) {
			// 같은 경우 로그인한 사용자의 추가 정보를 모델에 추가
			model.addAttribute("isCurrentUser", true);
			log.info("로그인유저==페이지유저");
		} else {
			model.addAttribute("isCurrentUser", false);
			log.info("로그인유저!=페이지유저");
		}
		
		// 모델에 잘 담겼는지 중간 확인용
		log.info("model = " + model);

		if (userEmail != null) {
			if(userEmail == "aaa@aaa.com") {
				log.info("확인용 임시 이메일입니다.");
			}
			// 팔로워 수 출력
			int followerCnt = followService.getFollowerValueByEmail(userEmail);
			System.out.println(followerCnt);
			model.addAttribute("followerCnt", followerCnt);
			// 팔로잉 수 출력
			int followingCnt = followService.getFollowingValueByEmail(userEmail);
			model.addAttribute("followingCnt", followingCnt);
			System.out.println(followingCnt);
			// 총 포스트 수 출력
			int reportValue = feedService.getReportValueById(userEmail);
			model.addAttribute("reportValue", reportValue);
			System.out.println(reportValue);
			
			// 잘 담겼나 확인용
			log.info("model = " + model);
		}
		
		return "feed";
	}
}