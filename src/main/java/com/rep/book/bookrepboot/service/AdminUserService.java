package com.rep.book.bookrepboot.service;

import com.rep.book.bookrepboot.dao.UserDao;
import com.rep.book.bookrepboot.dto.PageDTO;
import com.rep.book.bookrepboot.dto.UserDTO;
import com.rep.book.bookrepboot.util.MainUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AdminUserService {
    @Autowired
    private UserDao userDao;

    public boolean deleteUserByEmail(String userEmail) {
        log.info("deleteUserByEmail()");
        boolean result = false;
        try{
            userDao.resign(userEmail);
            log.info(userEmail + " 유저가 탈퇴되었습니다.");
            result = true;
        }catch (Exception e){
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public List<PageDTO> getUserByKeyword(String option, String keyword) {
        log.info("getUserByKeyword()");
        List<UserDTO> userList = new ArrayList<>();

        if (option.equals("email")){
            userList = userDao.getUserListByEmail(keyword);
        } else if (option.equals("name")) {
            userList = userDao.getUserListByName(keyword);
        } else {
            userList = null;
        }
        return MainUtil.setPaging(userList, 10);
    }
}
