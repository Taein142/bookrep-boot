package com.rep.book.bookrepboot.controller;

import com.rep.book.bookrepboot.dto.UserDTO;
import com.rep.book.bookrepboot.service.SuperAdminService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Slf4j
public class SuperAdminController {

    @Setter(onMethod_ = {@Autowired})
    private SuperAdminService superAdminService;

    @GetMapping("super-admin/index")
    public String superAdminIndex(){
        log.info("superAdminIndex()");

        return "th/superAdminMain";
    }

    @GetMapping("super-admin/user-manage")
    public String userManage(Model model){
        log.info("userManage()");

        List<UserDTO> userList = superAdminService.getUserToSuperAdmin();
        model.addAttribute("userList", userList);

        return "th/userManagement";
    }
    
}
