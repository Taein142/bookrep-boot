package com.rep.book.bookrepboot.service;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rep.book.bookrepboot.APIKEY;
import com.rep.book.bookrepboot.util.SecurityUtil;
import jakarta.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rep.book.bookrepboot.dao.UserDao;
import com.rep.book.bookrepboot.dto.UserDTO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

	public String[] applySignUp(String email,String domain, String name, String password, String address, String detail) throws IOException, ParseException, InterruptedException {
		log.info("applySignUp()");
		
		String msg = "회원가입 성공";
		String view = "redirect:/";
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		password = bCryptPasswordEncoder.encode(password);
		Map<String, Double> pointMap = getPointByAddress(address);
		Double longitude = pointMap.get("longitude");
		Double latitude = pointMap.get("latitude");
		String fullEmail = email + "@" + domain;
		UserDTO userDTO = new UserDTO(fullEmail, password, name, null, longitude, latitude, detail);
		
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

	public void modify(List<MultipartFile> files, String email, String name, String password, String address, String detail, HttpSession session) throws Exception {
		log.info("modify()");

		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		password = bCryptPasswordEncoder.encode(password);
		Map<String, Double> pointMap = getPointByAddress(address);
		Double longitude = pointMap.get("longitude");
		Double latitude = pointMap.get("latitude");

		UserDTO userDTO = new UserDTO(email, password, name, null, longitude, latitude, detail);

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

	public Map<String, Double> getPointByAddress(String address) throws IOException, InterruptedException, ParseException {
		HttpClient client = HttpClient.newHttpClient();
		String url = "https://api.vworld.kr/req/address?service=address&request=getCoord&key="
				+ APIKEY.GEO_KEY
				+ "&type=road"
				+ "&address=" + URLEncoder.encode(address, StandardCharsets.UTF_8);
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.GET()
				.build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		String body = response.body();
		System.out.println(body);
		JSONParser parser = new JSONParser();
		JSONObject object = (JSONObject) parser.parse(body);
		JSONObject res = (JSONObject) object.get("response");
		JSONObject result = (JSONObject) res.get("result");
		JSONObject point = (JSONObject) result.get("point");
		Double x = Double.parseDouble((String) point.get("x"));
		Double y = Double.parseDouble((String) point.get("y"));
		Map<String, Double> map = new HashMap<>();
		map.put("longitude", x);
		map.put("latitude", y);
		return map;
	}

	public String pwdChangeProc(String password, HttpSession session, RedirectAttributes rttr) {
		log.info("pwdChangeProc()");

		String email = session.getAttribute("email").toString();
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		password = bCryptPasswordEncoder.encode(password);
		Map<String, String> map = new HashMap<String, String>();
		map.put("email", email);
		map.put("password", password);
		try {
			userDao.pwdChangeProc(map);
			rttr.addFlashAttribute("msg", "비밀번호가 변경되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			rttr.addFlashAttribute("msg", "비밀번호 변경에 실패했습니다.");
		}
		session.removeAttribute("email");
		rttr.addFlashAttribute("msg", "비밀번호가 변경되었습니다.");

		return "redirect:/";
	}
}