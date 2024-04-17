package com.rep.book.bookrepboot.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;

public class SecurityAdminDTO extends User {

    private static final String ROLE_PREFIX = "ROLE_";
    private static final long serialVersionUID = 1L;

    public SecurityAdminDTO(AdminDTO adminDTO) {
        super(String.valueOf(adminDTO.getAdmin_id()), adminDTO.getAdmin_password(), makeGrantedAuthority(adminDTO.getAdmin_role()));
    }

    private static List<GrantedAuthority> makeGrantedAuthority(String role){
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority(ROLE_PREFIX + role));
        return list;
    }
}
