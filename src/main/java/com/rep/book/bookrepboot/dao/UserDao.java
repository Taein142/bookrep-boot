package com.rep.book.bookrepboot.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.ui.Model;

import com.rep.book.bookrepboot.dto.BookDTO;
import com.rep.book.bookrepboot.dto.UserDTO;
@Mapper
public interface UserDao {

	List<UserDTO> getUserList(String keyword);
	
	int signIn(Map<String, String> map);

	String getUserImage(String userEmail);

	int emailCheck(String email);

	void applySignUp(UserDTO userDTO);

	String getPassword(Map<String, String> map);

	UserDTO showModify(String email);

	void modify(UserDTO userDTO);

	void resign(String email);

	String getNameByEmail(String userEmail);

	Optional<UserDTO> findUserByEmail(String username);

	void pwdChangeProc(Map<String, String> map);

  List<UserDTO> getUserToSuperAdmin();

  UserDTO getFirstUser(String firUserEmail);

	UserDTO getSecondUser(String secUserEmail);
  
	List<UserDTO> getUserListByEmail(String keyword);

	List<UserDTO> getUserListByName(String keyword);
}
