package com.rep.book.bookrepboot;

import com.rep.book.bookrepboot.controller.AdminUserController;
import com.rep.book.bookrepboot.service.AdminUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AdminUserTest {
    @Autowired
    private AdminUserService adminUserService;

    @Test
    public void deleteUserTest(){
        String testUser = "del01@naver.com";
        boolean result = adminUserService.deleteUserByEmail(testUser);
        System.out.println(result);
    }
}
