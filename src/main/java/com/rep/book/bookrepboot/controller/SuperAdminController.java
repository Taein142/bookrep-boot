package com.rep.book.bookrepboot.controller;

import com.google.gson.Gson;
import com.rep.book.bookrepboot.dto.PathDTO;
import com.rep.book.bookrepboot.dto.UserDTO;
import com.rep.book.bookrepboot.service.SuperAdminService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

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

    @GetMapping("super-admin/delivery-manage")
    public String deliveryManage(Model model){
        log.info("deliveryManage()");

        superAdminService.getPathToSuperAdmin(model);

        superAdminService.getAllAdminList(model);

        return "th/deliveryManagement";
    }

    @PostMapping("super-admin/match-driver")
    public String matchDriver(@RequestParam("path-id") Long pathId, @RequestParam("admin-id") Long adminId, @RequestParam("current-admin") String currentAdminId){
        log.info("matchDriver()");

        superAdminService.matchDriver(pathId, adminId);
        superAdminService.rollbackToAdmin(currentAdminId);
        return "redirect:/super-admin/delivery-manage";
    }

    @GetMapping("super-admin/delivery-detail")
    public String deliveryDetail(@RequestParam("path-id") Long pathId, Model model){
        log.info("deliveryDetail()");

        superAdminService.getPathDetail(pathId, model);

        return "th/deliveryDetail";
    }
    
}
