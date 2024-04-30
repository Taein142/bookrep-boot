package com.rep.book.bookrepboot.service;

import com.rep.book.bookrepboot.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
