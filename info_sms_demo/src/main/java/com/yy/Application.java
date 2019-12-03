package com.yy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.UnsupportedEncodingException;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        String s = "123";
        try {
            String str = new String(s.getBytes(""),"");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
