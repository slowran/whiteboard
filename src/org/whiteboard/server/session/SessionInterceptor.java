package org.whiteboard.server.session;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.whiteboard.server.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by 李浩然 on 2017/5/17.
 */
@Component("SpringMVCInterceptor")
public class SessionInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();

        // 过滤登录、退出访问
        String[] noFilters = new String[] { "/user/login", "/user/logout", "/user/register" };

        String uri = request.getRequestURI();

        String userId = (String)session.getAttribute(SessionContext.ATTR_USER_ID);

        for (String s : noFilters) {
            if(uri.contains(s)){
                return true;
            }
        }

        if(userId == null) {
            return false;
        }

        return true;
    }
}
