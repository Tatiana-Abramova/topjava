package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {
    private static final Logger log = LoggerFactory.getLogger(SecurityUtil.class);
    private static int userId;

    public static int authUserId() {
        return userId;
    }

    public static void setAuthUserId(int userId) {
        SecurityUtil.userId = userId;
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }

    public static void authenticate(HttpServletRequest request) {
        String user = request.getParameter("user");
        if (user != null) {
            SecurityUtil.setAuthUserId(Integer.parseInt(user));
            log.info("Logged in with userId=" + user);
            request.setAttribute("user", SecurityUtil.authUserId());
        }
    }
}