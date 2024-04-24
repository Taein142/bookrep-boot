package com.rep.book.bookrepboot.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rep.book.bookrepboot.dto.UserDTO;
import com.rep.book.bookrepboot.service.SignService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class SignController {

	@Autowired
	SignService signService;

	// 로그인 이동
	@GetMapping("/user/sign-in")
	public String showSignIn() {
		log.info("로그인 화면 이동");
		return "signIn";
	}

	// 로그인 로직
//	@PostMapping("sign-in-proc")
//	public String signIn(HttpSession session, @RequestParam String username, @RequestParam String password) {
//		log.info("email:{}, pw:{}", username, password);
//
//		String view = signService.signIn(session, username, password);
//		log.info(view);
//		return view;
//	}

	// 로그아웃 로직
	@GetMapping("user/sign-out")
	public String signOut(HttpSession session) {
		session.invalidate();
		log.info("로그아웃");
		return "home1";
	}
	
	@GetMapping("sign-up")
	public String signUp() {
		log.info("signUp()");
		
		return "signUp";
	}
	
	@PostMapping("/emailCheck")
	@ResponseBody
	public String emailCheck(@RequestParam("email") String email) {
		log.info("emailCheck()");
		
		int cnt = signService.emailCheck(email);
		
		return String.valueOf(cnt);
	}
	
	@PostMapping("sign-up")
	public String applySignUp(
			@RequestParam("email") String email,
			@RequestParam("domain") String domain,
			@RequestParam("domainList") String domainList,
			@RequestParam("name") String name,
			@RequestParam("password") String password,
			@RequestParam("address") String address,
			@RequestParam("detail") String detail,
			@RequestParam("agreedToTerms") boolean agreedToTerms,
			@RequestParam("agreedToPrivacyPolicy") boolean agreedToPrivacyPolicy,
			RedirectAttributes rttr) throws IOException, ParseException, InterruptedException {
		
		log.info("applySignUp()");

		if (!(agreedToTerms && agreedToPrivacyPolicy)){
			String msg = "약관 및 개인정보 처리방침에 동의하지 않으면 회원가입할 수 없습니다.";
			rttr.addFlashAttribute("msg", msg);
			return "home";
		}

		String[] arr =  signService.applySignUp(email,domain, name, password, address, detail);
		
		rttr.addFlashAttribute("msg", arr[0]);
		
		return arr[1];
	}
	
	@GetMapping("find-password")
	public String showPassFinder() {
		log.info("showPassFinder");
		
		return "findPassword";
	}
	
	@PostMapping(value = "find-password", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String findPassword(@RequestParam("email") String email, @RequestParam("name") String name) {
		log.info("findPassword");
		
		String result = signService.findPassword(email, name);
		
		return result;
	}
	
	@GetMapping("user/update")
	public String showModify(HttpSession session, Model model) {
		log.info("showModify()");
		
		UserDTO userDTO = signService.showModify(session);
		model.addAttribute("user", userDTO);
		
		return "update";
	}
	
	@PostMapping("user/update")
	public String modify(@RequestPart List<MultipartFile> files, UserDTO userDTO, HttpSession session) throws Exception {
		log.info("modify()");
		
		signService.modify(files, userDTO, session);
		
		return "redirect:/";
	}
	
	@GetMapping("user/resign")
	public String resign(HttpSession session) {
		log.info("resign()");
		
		signService.resign(session);
		
		return "redirect:/";
	}

	@GetMapping("pwd-change")
	public String pwdChange(){
		log.info("pwdChange");

		return "th/pwdChange";
	}

	@PostMapping("pwd-change-proc")
	public String pwdChangeProc(@RequestParam String password,
								@RequestParam String fucku,
								HttpSession session,
								RedirectAttributes rttr) {
		log.info("pwdChangeProc");

		return signService.pwdChangeProc(password, session, rttr);
	}
	
	
	
}