package com.rep.book.bookrepboot.dao;

import com.rep.book.bookrepboot.dto.PathDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PathDao {

    void insertPath(PathDTO pathDTO);


    List<PathDTO> getPathToSuperAdmin();

    void matchDriver(PathDTO pathDTO);

    PathDTO getPathDetail(Long pathId);

    List<PathDTO> getPathByAdminId(Long adminId);

    void completeDelivery(Long pathId);

}
