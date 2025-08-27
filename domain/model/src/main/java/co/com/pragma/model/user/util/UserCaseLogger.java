package co.com.pragma.model.user.util;

public interface UserCaseLogger {

    void trace(String message, Object ... args);

    void info(String message, Object ... args);

    void warn(String message, Object ... args);

    void error(String message, Object ... args);

}
