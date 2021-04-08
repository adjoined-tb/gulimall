package me.adjoined.gulimall.order.interceptor;

import me.adjoined.common.constant.AuthServerConstant;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class LoginUserInterceptor implements HandlerInterceptor {
    public static ThreadLocal<String> loginUser = new ThreadLocal<>();
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute(AuthServerConstant.LOGIN_USER);
        if (userId != null) {
            loginUser.set(userId);
            return true;
        } else {
            request.getSession().setAttribute("msg", "please log in first");
            response.sendRedirect("http://auth.adjoined.me/auth/auth/login");
            return false;
        }
    }

}
