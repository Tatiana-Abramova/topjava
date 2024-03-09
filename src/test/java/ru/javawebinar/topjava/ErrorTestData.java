package ru.javawebinar.topjava;

import ru.javawebinar.topjava.util.exception.ErrorInfo;
import ru.javawebinar.topjava.util.exception.ErrorType;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;
import ru.javawebinar.topjava.web.user.ProfileRestController;

import java.util.List;

import static ru.javawebinar.topjava.MealTestData.MEAL1_ID;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;

public class ErrorTestData {
    public static MatcherFactory.Matcher<ErrorInfo> ERROR_MATCHER = MatcherFactory.usingIgnoringCollectionOrderComparator(ErrorInfo.class);

    public static final String MEAL_REST_URL = "http://localhost" + MealRestController.REST_URL + "/";

    public static final String ADMIN_REST_URL = "http://localhost" + AdminRestController.REST_URL + "/";

    public static final String USER_REST_URL = "http://localhost" + ProfileRestController.REST_URL;


    public static final ErrorInfo createMealErrorInfo = new ErrorInfo(MEAL_REST_URL, ErrorType.VALIDATION_ERROR, List.of("[dateTime] must not be null", "[description] must not be blank", "[calories] must not be null"));

    public static final ErrorInfo updateMealErrorInfo = new ErrorInfo(MEAL_REST_URL + MEAL1_ID, ErrorType.VALIDATION_ERROR, List.of("[dateTime] must not be null", "[description] must not be blank", "[calories] must not be null"));

    public static final ErrorInfo createAdminErrorInfo = new ErrorInfo(ADMIN_REST_URL, ErrorType.VALIDATION_ERROR, List.of("[password] must not be blank", "[email] must not be blank", "[password] size must be between 5 and 128", "[name] must not be blank"));

    public static final ErrorInfo updateAdminErrorInfo = new ErrorInfo(ADMIN_REST_URL + ADMIN_ID, ErrorType.VALIDATION_ERROR, List.of("[password] must not be blank", "[email] must not be blank", "[password] size must be between 5 and 128", "[name] must not be blank"));

    public static final ErrorInfo createUserErrorInfo = new ErrorInfo(USER_REST_URL, ErrorType.VALIDATION_ERROR, List.of("[password] must not be blank", "[email] must not be blank", "[password] Length must be between 5 and 32", "[name] must not be blank"));

    public static final ErrorInfo updateUserErrorInfo = new ErrorInfo(USER_REST_URL, ErrorType.VALIDATION_ERROR, List.of("[password] must not be blank", "[email] must not be blank", "[password] Length must be between 5 and 32", "[name] must not be blank"));

    public static final ErrorInfo duplicateDatetimeCreateMealErrorInfo = new ErrorInfo(MEAL_REST_URL, ErrorType.DATA_ERROR, List.of("Meal with this datetime already exists"));

    public static final ErrorInfo duplicateDatetimeUpdateMealErrorInfo = new ErrorInfo(MEAL_REST_URL + MEAL1_ID, ErrorType.DATA_ERROR, List.of("Meal with this datetime already exists"));

    public static final ErrorInfo duplicateEmailCreateUserErrorInfo = new ErrorInfo(USER_REST_URL, ErrorType.DATA_ERROR, List.of("User with this email already exists"));

    public static final ErrorInfo duplicateEmailUpdateUserErrorInfo = new ErrorInfo(USER_REST_URL, ErrorType.DATA_ERROR, List.of("User with this email already exists"));

    public static final ErrorInfo duplicateEmailCreateAdminErrorInfo = new ErrorInfo(ADMIN_REST_URL, ErrorType.DATA_ERROR, List.of("User with this email already exists"));

    public static final ErrorInfo duplicateEmailUpdateAdminErrorInfo = new ErrorInfo(ADMIN_REST_URL + ADMIN_ID, ErrorType.DATA_ERROR, List.of("User with this email already exists"));
}
