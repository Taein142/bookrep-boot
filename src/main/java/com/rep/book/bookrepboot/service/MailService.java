package com.rep.book.bookrepboot.service;

import com.rep.book.bookrepboot.dao.UserDao;
import com.rep.book.bookrepboot.dto.UserDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
@Slf4j
public class MailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private UserDao userDao;

    private String authNum;

    public void createCode(){
        log.info("createCode()");

        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 8; i++){
            int index = random.nextInt(3);

            switch (index){
                case 0:
                    key.append((char) (random.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) (random.nextInt(26) + 65));
                    break;
                case 2:
                    key.append((char) (random.nextInt(9) + 48));
                    break;
            }
        }
        authNum = key.toString();
    }
    public MimeMessage createEmailForm(String email)
            throws MessagingException, UnsupportedEncodingException {
        log.info("createEmailForm()");

        String setFrom = "dbals9926@naver.com";
        String title = "인증 코드 입니다";

        MimeMessage mimeMessage = emailSender.createMimeMessage();
        mimeMessage.addRecipients(MimeMessage.RecipientType.TO, email);
        mimeMessage.setFrom(setFrom);
        mimeMessage.setSubject(title);
        mimeMessage.setText(setContext(), "utf-8", "html");

        return mimeMessage;
    }

    private String setContext(){
        log.info("setContext()");

        createCode();
        Context context = new Context();
        context.setVariable("code", authNum);

        return templateEngine.process("th/mailForm", context);
    }

    public String sendEmail(UserDTO userDTO, HttpSession httpSession){
        log.info("sendEmail()");

        MimeMessage emailForm = null;
        String res = null;
        String email = null;

        try {
            email = userDTO.getEmail();
                emailForm = createEmailForm(email);
                emailSender.send(emailForm);
                res = "ok";
                httpSession.setAttribute("authNum", authNum);
                httpSession.setAttribute("email", userDTO.getEmail());
        }catch (Exception e){
            e.printStackTrace();
            res = "fail";
        }



        return res;
    }

    public String codeAuth(String vCode, HttpSession session) {
        log.info("codeAuth()");

        String authNum = (String) session.getAttribute("authNum");
        String res = null;
        if (vCode.equals(authNum)){
            res = "ok";
        }else {
            res = "fail";
        }

        session.removeAttribute("authNum");

        return res;
    }
}
