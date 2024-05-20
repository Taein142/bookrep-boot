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

	// 로그아웃 로직
	@GetMapping("sign-out")
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
			@RequestParam(required = false) String agreedToTerms,
			@RequestParam(required = false) String agreedToPrivacyPolicy,
			RedirectAttributes rttr) throws IOException, ParseException, InterruptedException {
		
		log.info("applySignUp()");

		if (agreedToTerms == null || agreedToPrivacyPolicy == null ||
				!agreedToTerms.equals("agree") || !agreedToPrivacyPolicy.equals("agree")) {
			rttr.addFlashAttribute("msg", "약관에 동의해주세요.");
			return "redirect:/sign-up";
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
	public String modify(@RequestPart List<MultipartFile> files,
						 @RequestParam("email") String email,
						 @RequestParam("name") String name,
						 @RequestParam("password") String password,
						 @RequestParam("address") String address,
						 @RequestParam("detail") String detail,
						 @RequestParam("mailConfirm") String mailConfirm,
						 HttpSession session, RedirectAttributes rttr) throws Exception {
		log.info("modify()");

		if (mailConfirm == null || !mailConfirm.equals("true")) {
			rttr.addFlashAttribute("msg", "메일인증 해주세요");
			return "redirect:/user/update";
		}

		try {
			signService.modify(files, email, name, password, address, detail, session);
			rttr.addFlashAttribute("msg", "회원정보 수정이 완료되었습니다.");
		} catch (Exception e){
			rttr.addFlashAttribute("msg", "회원정보 수정이 실패하였습니다.");
			return "redirect:/user/update";
		}

		return "redirect:/user/feed/" + email;
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

	@GetMapping("sign-in-terms")
	public String showSigInInTerms(){
		log.info("showTerms");
		return "th/signInTerms";
	}

	@GetMapping("sign-in-privacy-policy")
	public String showSignInPrivacyPolicy(){
		log.info("showPrivacyPolicy()");
		return "th/signInPrivacyPolicy";
	}
	
}