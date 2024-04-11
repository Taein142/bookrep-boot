package com.rep.book.bookrepboot.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rep.book.bookrepboot.util.SecurityUtil;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rep.book.bookrepboot.dao.UserDao;
import com.rep.book.bookrepboot.dto.UserDTO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SignService {
	
	@Autowired 
	private UserDao userDao;

	public String signIn(HttpSession session, String email, String password) {
		log.info("signIn()");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("email", email);
		map.put("password", password);
		int loginResult = userDao.signIn(map);
		
		if(loginResult != 0) {
			log.info("로그인 성공");
			
			session.setAttribute("email", email);
			
			return "redirect:/home";
			
		} else {
			log.info("로그인 실패");
			return "signIn";
		}
		
	}

	public int emailCheck(String email) {
		log.info("emailCheck()");
		
		int cnt = userDao.emailCheck(email);
		return cnt;
	}

	public String[] applySignUp(String email, String name, String password, Double longitude, Double latitude) {
		log.info("applySignUp()");
		
		String msg = "회원가입 성공";
		String view = "redirect:/";
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		password = bCryptPasswordEncoder.encode(password);
		UserDTO userDTO = new UserDTO(email, password, name, null, longitude, latitude);
		
		try {
			userDao.applySignUp(userDTO);
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = "중복된 이메일입니다";
			view = "redirect:sign-up";
		}
		String[] arr = {msg, view};
		
		return arr;
	}

	public String findPassword(String email, String name) {
		log.info("findPassword()");
		
		String result = null;
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("email", email);
		map.put("name", name);
		String password = userDao.getPassword(map);
		if (password == null || password.isEmpty()) {
			result = "입력정보가 틀렸습니다.";
		}else {
			result = "당신의 비밀번호는 " + password + " 입니다.";
		}
		
		return result;
	}

	public UserDTO showModify(HttpSession session) {
		log.info("showModify()");
		
		String email = SecurityUtil.getCurrentUserEmail();
		UserDTO userDTO = userDao.showModify(email);
		
		return userDTO;
	}

	public void modify(List<MultipartFile> files, UserDTO userDTO, HttpSession session) throws Exception {
		log.info("modify()");
		String upFile = null;
		if (files != null && !files.isEmpty()) {
			upFile = files.get(0).getOriginalFilename();
		}
		if (upFile != null && !upFile.isEmpty() && !upFile.isBlank()) {
			fileUpload(files, session, userDTO);
		}else {
			userDTO.setImage("noimage.png");
		}
		
		userDao.modify(userDTO);
		
	}
	
	private void fileUpload(List<MultipartFile> files,
            HttpSession session,
            UserDTO userDTO) throws Exception {
		log.info("fileUpload()");
		String image = null;
		String original = null;
		
		String realPath = "C:/Users/dbals/git/bookrep/semi-bookrep/src/main/webapp/";
		log.info(realPath);
		realPath += "resources/images/";
		File folder = new File(realPath);
		if(folder.isDirectory() == false){
		folder.mkdir();
		}
		
		MultipartFile mf = files.get(0);
		original = mf.getOriginalFilename();
		
		image = System.currentTimeMillis()
				+ original.substring(original.lastIndexOf("."));
		
		File file = new File(realPath + image);
	
		mf.transferTo(file);
		userDTO.setImage(image);
	}

	public void resign(HttpSession session) {
		log.info("resign()");
		
		String email = SecurityUtil.getCurrentUserEmail();
		
		userDao.resign(email);
		
		session.invalidate();
	}

}