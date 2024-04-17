package com.rep.book.bookrepboot;

import com.rep.book.bookrepboot.dao.UserDao;
import com.rep.book.bookrepboot.service.SignService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class SignServiceTest {

    @InjectMocks
    private SignService signService;

    @Mock
    private UserDao userDao;

    private MockHttpSession session;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        session = new MockHttpSession();
    }

    @Test
    public void 로그인_성공_테스트() {
        Map<String, String> map = new HashMap<>();
        map.put("email", "test@test.com");
        map.put("password", "password");
        when(userDao.signIn(map)).thenReturn(1);
        String result = signService.signIn(session, "test@test.com", "password");
        assertEquals("redirect:/user/home", result);
    }

    @Test
    public void 로그인_실패_테스트() {
        Map<String, String> map = new HashMap<>();
        map.put("email", "test@test.com");
        map.put("password", "password");
        when(userDao.signIn(map)).thenReturn(0);
        String result = signService.signIn(session, "test@test.com", "password");
        assertEquals("signIn", result);
    }

    @Test
    public void 이메일_중복_테스트() {
        when(userDao.emailCheck("test@test.com")).thenReturn(1);
        int result = signService.emailCheck("test@test.com");
        assertEquals(1, result);
    }

    @Test
    public void 이메일_중복_아님_테스트() {
        when(userDao.emailCheck("test@test.com")).thenReturn(0);
        int result = signService.emailCheck("test@test.com");
        assertEquals(0, result);
    }

    @Test
    public void 비밀번호_찾기_테스트() {
        Map<String, String> map = new HashMap<>();
        map.put("email", "test@test.com");
        map.put("name", "name");
        when(userDao.getPassword(map)).thenReturn("password");
        String result = signService.findPassword("test@test.com", "name");
        assertEquals("당신의 비밀번호는 password 입니다.", result);
    }

    @Test
    public void 비밀번호_찾기_실패_테스트() {
        Map<String, String> map = new HashMap<>();
        map.put("email", "test@test.com");
        map.put("name", "name");
        when(userDao.getPassword(map)).thenReturn(null);
        String result = signService.findPassword("test@test.com", "name");
        assertEquals("입력정보가 틀렸습니다.", result);
    }

    @Test
    public void 비밀번호_변경_테스트() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = bCryptPasswordEncoder.encode("newPassword");
        Map<String, String> map = new HashMap<>();
        map.put("email", "test@test.com");
        map.put("password", password);
        doNothing().when(userDao).pwdChangeProc(map);
        String result = signService.pwdChangeProc("newPassword", session, null);
        assertEquals("redirect:/", result);
    }

    @Test
    public void 비밀번호_변경_실패_테스트() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = bCryptPasswordEncoder.encode("newPassword");
        Map<String, String> map = new HashMap<>();
        map.put("email", "test@test.com");
        map.put("password", password);
        doNothing().when(userDao).pwdChangeProc(map);
        String result = signService.pwdChangeProc("newPassword", session, null);
        assertEquals("redirect:/", result);
    }
}
