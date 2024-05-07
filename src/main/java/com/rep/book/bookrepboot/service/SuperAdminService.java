package com.rep.book.bookrepboot.service;

import com.rep.book.bookrepboot.dao.UserDao;
import com.rep.book.bookrepboot.dto.UserDTO;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SuperAdminService {

    @Setter(onMethod_ = {@Autowired})
    private UserDao userDao;

    public List<UserDTO> getUserToSuperAdmin() {
        log.info("getUserToSuperAdmin()");

        return userDao.getUserToSuperAdmin();
    }
}
