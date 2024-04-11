package com.rep.book.bookrepboot.service;

import com.rep.book.bookrepboot.dao.UserDao;
import com.rep.book.bookrepboot.dto.SecurityUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDao userDao;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return
                Optional.ofNullable(userDao.findUserByEmail(username))
                        .filter(m -> m != null)
                        .map(SecurityUserDTO::new).get();
    }

}
