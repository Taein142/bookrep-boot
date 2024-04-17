package com.rep.book.bookrepboot.controller;

import java.util.ArrayList;
import java.util.List;

import com.rep.book.bookrepboot.util.SecurityUtil;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rep.book.bookrepboot.dto.UserDTO;
import com.rep.book.bookrepboot.service.FollowService;
import com.rep.book.bookrepboot.util.MainUtil;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class FollowController {
	
	@Autowired
	private FollowService followService;
	
	
	
	@GetMapping("user/following/{email}")
	public String showFollowing(@PathVariable String email,
								Model model,
								@RequestParam(required = false) Integer pageNum) {
		log.info("showFollowing()");
		
		List<String> followingList = followService.getFollowingByEmail(email);
		Integer currentPage = 1;
		if (pageNum != null) {
			currentPage = pageNum;
		}
		List<String> currentFollowingList = MainUtil.setPagingFollow(followingList, currentPage);
		
		List<UserDTO> currentFollowingUserList = new ArrayList<UserDTO>();
		for (String userEmail : currentFollowingList) {
			currentFollowingUserList.add(followService.getUserByEmail(userEmail));
		}
		
		model.addAttribute("followingList", currentFollowingUserList);
		model.addAttribute("currentPageNum", currentPage);
		model.addAttribute("totalFollowingSize", followingList.size());
		model.addAttribute("email", email);
		return "following";
	}
	
	@GetMapping("user/follower/{email}")
	public String showFollower(@PathVariable String email,
								Model model,
								@RequestParam(required = false) Integer pageNum) {
		log.info("showFollower()");
		
		List<String> followerList = followService.getFollowerByEmail(email);
		Integer currentPage = 1;
		if (pageNum != null) {
			currentPage = pageNum;
		}
		List<String> currentFollowerList = MainUtil.setPagingFollow(followerList, currentPage);
		
		List<UserDTO> currentFollowerUserList = new ArrayList<UserDTO>();
		for (String userEmail : currentFollowerList) {
			currentFollowerUserList.add(followService.getUserByEmail(userEmail));
		}
		
		model.addAttribute("followerList", currentFollowerUserList);
		model.addAttribute("currentPageNum", currentPage);
		model.addAttribute("totalFollowerSize", followerList.size());
		model.addAttribute("email", email);
		
		return "follower";
	}
	
	@PostMapping("/user/follow")
	@ResponseBody
	public String follow(@RequestParam("email") String followerEmail, HttpSession session) {
		log.info("follow()");
		
		String followeeEmail = SecurityUtil.getCurrentUserEmail();
		
		followService.follow(followerEmail, followeeEmail);
		
		return "redirect:user/feed/" + followerEmail;
	}

	@PostMapping("/user/unfollow")
	@ResponseBody
	public String unfollow(@RequestParam("email") String followerEmail, HttpSession session) {
		log.info("unfollow()");
		
		String followeeEmail = SecurityUtil.getCurrentUserEmail();
		
		followService.unfollow(followerEmail, followeeEmail);
		
		return "redirect:user/feed/" + followerEmail;
	}
	
}
