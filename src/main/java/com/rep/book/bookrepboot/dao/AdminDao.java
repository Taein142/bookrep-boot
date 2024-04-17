package com.rep.book.bookrepboot.dao;

import com.rep.book.bookrepboot.dto.AdminDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminDao {

    AdminDTO findUserByEmail(String username);

    void adminUp(AdminDTO adminDTO);
}
