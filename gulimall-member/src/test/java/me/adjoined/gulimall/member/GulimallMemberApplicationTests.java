package me.adjoined.gulimall.member;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.Md5Crypt;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class GulimallMemberApplicationTests {

    @Test
    void contextLoads() {
        String s = DigestUtils.md5Hex("abcd");
        System.out.println(s);

        String s1 = Md5Crypt.md5Crypt("abcd".getBytes());
        System.out.println(s1);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode("123456");
        if (passwordEncoder.matches("123456", encode)) {
            System.out.println("matched!");
        }
    }

}
