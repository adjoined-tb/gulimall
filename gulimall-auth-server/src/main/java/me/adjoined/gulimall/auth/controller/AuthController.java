package me.adjoined.gulimall.auth.controller;

import me.adjoined.common.utils.R;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("auth/auth")
public class AuthController {
    @RequestMapping("/test")
    public R test() {
        return R.ok().put("greeting", "yoyoyo");
    }

    @RequestMapping("/login")
    @ResponseBody
    public String login(HttpSession session) {
        session.setAttribute("loginUser", "tianbian");
        return "logged in.";
    }
}
