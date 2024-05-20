package com.rep.book.bookrepboot;

import com.rep.book.bookrepboot.service.SuperAdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@SpringBootTest
public class SuperAdminTest {

    @Autowired
    private SuperAdminService superAdminService;

    @Test
    public void convertIsbnToBookNameTest() throws IOException, InterruptedException, ParserConfigurationException {
        String xml = superAdminService.convertIsbnToBookName("9788925560663");
        System.out.println(xml);
    }
}
