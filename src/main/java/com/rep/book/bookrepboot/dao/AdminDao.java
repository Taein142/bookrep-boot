package com.rep.book.bookrepboot.dao;

import com.rep.book.bookrepboot.dto.AdminDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AdminDao {

    Optional<AdminDTO> findUserByEmail(String username);

    void adminUp(AdminDTO adminDTO);

    void appointDriver(Long adminId);

    List<AdminDTO> getAllAdminList();

    void rollbackToAdmin(Long currentAdminId);
}
