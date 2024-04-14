package com.rep.book.bookrepboot;

import com.rep.book.bookrepboot.service.SignService;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Map;

@SpringBootTest
public class GeoTest {

    @Autowired
    SignService signService;

    @Test
    public void getPointByAddressTest() throws IOException, InterruptedException, ParseException {
        Map<String, Double> map = signService.getPointByAddress("남동대로 370번길 122");
        System.out.println("longitude: " + map.get("longitude"));
        System.out.println("latitude: " + map.get("latitude"));
    }
}
