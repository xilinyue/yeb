package com.ztt.server;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author LiuSu
 * @create 2021/3/12 20:05
 */
public class TestJava {
    public static void main(String args[]) {
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String str = passwordEncoder.encode("123456");
//        System.out.println(str);
        String a = "dsfas.png";
        System.out.println(a.substring(a.lastIndexOf(".")));
    }
}
