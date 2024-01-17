package ru.javawebinar.topjava;

import org.springframework.util.ClassUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Profiles {

    public static final String
            JDBC = "jdbc",
            JPA = "jpa",
            DATAJPA = "datajpa";

    public static final String REPOSITORY_IMPLEMENTATION = DATAJPA;

    public static final String
            POSTGRES_DB = "postgres",
            HSQL_DB = "hsqldb";

    //  Get DB profile depending of DB driver in classpath
    public static String getActiveDbProfile() {
        if (ClassUtils.isPresent("org.postgresql.Driver", null)) {
            return POSTGRES_DB;
        } else if (ClassUtils.isPresent("org.hsqldb.jdbcDriver", null)) {
            return HSQL_DB;
        } else {
            throw new IllegalStateException("Could not find DB driver");
        }
    }

    public static String getActiveRepositoryProfile() {
        final String activeProfile = System.getProperty("spring.profiles.active");
        if (activeProfile == null) {
            return REPOSITORY_IMPLEMENTATION;
        }
        Matcher matcher = Pattern.compile(JDBC + "|" + JPA + "|" + DATAJPA).matcher(activeProfile);
        return matcher.find() ? matcher.group() : REPOSITORY_IMPLEMENTATION;
    }
}
