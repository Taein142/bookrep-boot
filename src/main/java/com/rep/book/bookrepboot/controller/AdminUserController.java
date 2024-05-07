package com.rep.book.bookrepboot.controller;

import com.rep.book.bookrepboot.dto.PageDTO;
import com.rep.book.bookrepboot.dto.UserDTO;
import com.rep.book.bookrepboot.service.AdminUserService;
import com.rep.book.bookrepboot.service.SearchService;
import com.rep.book.bookrepboot.util.MainUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
public class AdminUserController {
    @Autowired
    private SearchService searchService;
    @Autowired
    private AdminUserService adminUserService;

    //유저 관리 페이지 (이름, 이메일)
    @GetMapping("admin/main-user-management")
    public String showUserManagement(@RequestParam(value = "keyword", required = false) String keyword,
                                     @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                     Model model){
        log.info("showUserManagement()");
        List<UserDTO> userList = searchService.getUserByString(keyword);
        List<PageDTO> userPagingList = MainUtil.setPaging(userList, 10);

        if (userPagingList.size() > 0){
            model.addAttribute("userList", userPagingList.get(pageNum-1));
        } else {
            model.addAttribute("userList", null);
        }
        model.addAttribute("currentPageNum", pageNum);
        model.addAttribute("listMaxSize", userPagingList.size());

        return "th/userManagement";
    }

    // 유저 강제탈퇴
    @PostMapping("/admin/user-delete")
    @ResponseBody
    public String deleteUser(@RequestParam(value = "userEmail") String userEmail) {
        log.info("deleteUser()");
        try {
            boolean success = adminUserService.deleteUserByEmail(userEmail);
            if (success) {
                log.info(userEmail + " 유저가 성공적으로 삭제되었습니다.");
                return "ok";
            } else {
                log.error(userEmail + " 유저 삭제에 실패했습니다.");
                return "fail";
            }
        } catch (Exception e) {
            log.error("유저 삭제 중 오류가 발생했습니다.", e);
            return "fail";
        }
    }
}
