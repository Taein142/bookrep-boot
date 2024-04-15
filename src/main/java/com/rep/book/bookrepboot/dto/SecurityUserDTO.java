package com.rep.book.bookrepboot.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;

public class SecurityUserDTO extends User {

    private static final String ROLE_PREFIX = "ROLE_";
    private static final long serialVersionUID = 1L;

    public SecurityUserDTO(UserDTO userDTO) {
        super(userDTO.getEmail(), userDTO.getPassword(), makeGrantedAuthority(userDTO.getRole()));
    }

    private static List<GrantedAuthority> makeGrantedAuthority(String role){
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority(ROLE_PREFIX + role));
        return list;
    }

}
