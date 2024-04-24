package com.rep.book.bookrepboot.service;

import com.rep.book.bookrepboot.dao.AdminDao;
import com.rep.book.bookrepboot.dao.UserDao;
import com.rep.book.bookrepboot.dto.SecurityAdminDTO;
import com.rep.book.bookrepboot.dto.SecurityUserDTO;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    public CustomUserDetailsService() {
        log.info("CustomUserDetailServiceForUser");
    }

    @Setter(onMethod_ = {@Autowired})
    private UserDao userDao;

    @Setter(onMethod_ = {@Autowired})
    private AdminDao adminDao;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername");
        if (username.contains("@")){
            return
                    Optional.ofNullable(userDao.findUserByEmail(username))
                            .filter(m -> m != null)
                            .map(SecurityUserDTO::new).get();
        }else {
            return
                    Optional.ofNullable(adminDao.findUserByEmail(username))
                            .filter(m -> m != null)
                            .map(SecurityAdminDTO::new).get();
        }

    }

}
