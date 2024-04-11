package com.rep.book.bookrepboot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rep.book.bookrepboot.dao.FollowDao;
import com.rep.book.bookrepboot.dao.UserDao;
import com.rep.book.bookrepboot.dto.FollowDTO;
import com.rep.book.bookrepboot.dto.UserDTO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FollowService {

	@Autowired
	private FollowDao followDao;
	
	@Autowired
	private UserDao userDao;

	public Integer getFollowerValueByEmail(String userEmail) {
		
		log.info("getFollowerValueById()");
		
		// 페이지 유저에 따른 팔로워 리스트 가져오기
		List<String> followerList = getFollowerByEmail(userEmail);

		if (followerList == null || followerList.isEmpty()) {
	        return 0;
	    }
		
		// 팔로워 수 저장
		Integer followerCnt = followerList.size();

		return followerCnt;
	}

	public int getFollowingValueByEmail(String userEmail) {
		// 페이지 유저에 따른 팔로잉 리스트 가져오기
		List<String> followingList = getFollowingByEmail(userEmail);

        if (followingList == null || followingList.isEmpty()) {
            return 0;
        }
		
		// 팔로잉 수 저장
		int followingCnt = followingList.size();

		return followingCnt;
	}

	public List<String> getFollowerByEmail(String email) {
		log.info("getFollowerByEmail()");
		
		List<String> followerList = followDao.getFollowerByEmail(email);
		
		return followerList;
	}

	public List<String> getFollowingByEmail(String email) {
		log.info("getFollowingByEmail()");
		
		List<String> followingList = followDao.getFollowingByEmail(email);
		
		return followingList;
	}

	public void follow(String followerEmail, String followeeEmail) {
		log.info("follow()");
		
		FollowDTO followDTO = new FollowDTO(followerEmail, followeeEmail);
		followDao.follow(followDTO);
	}

	public void unfollow(String followerEmail, String followeeEmail) {
		log.info("unfollow()");
		
		FollowDTO followDTO = new FollowDTO(followerEmail, followeeEmail);
		followDao.unfollow(followDTO);
	}
	
	public boolean isFollowing(String followerEmail, String followeeEmail) {
		log.info("isFollowing()");
		
		FollowDTO followDTO = new FollowDTO(followerEmail, followeeEmail);
		int followValue = followDao.isFollowing(followDTO);
		
		if (followValue == 0) {
			return false;
		}else {
			return true;
		}
	}

	public UserDTO getUserByEmail(String userEmail) {
		log.info("getUserByEmail()");
		return userDao.showModify(userEmail);
	}
}
