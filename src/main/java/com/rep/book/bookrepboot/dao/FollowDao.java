package com.rep.book.bookrepboot.dao;

import java.util.List;

import com.rep.book.bookrepboot.dto.FollowDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FollowDao {

	List<String> getFollowerByEmail(String email);

	List<String> getFollowingByEmail(String email);

	int isFollowing(FollowDTO followDTO);

	void follow(FollowDTO followDTO);

	void unfollow(FollowDTO followDTO);


}
