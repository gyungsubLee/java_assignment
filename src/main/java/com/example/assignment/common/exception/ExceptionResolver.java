package com.example.assignment.common.exception;

import java.util.HashMap;
import java.util.Map;

public class ExceptionResolver {

    private static final Map<Class<? extends Throwable>, ErrorCode> exceptionMap = new HashMap<>();

    static {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.getExceptionClass() != null) {
                exceptionMap.put(errorCode.getExceptionClass(), errorCode);
            }
        }
    }

    public static ErrorCode resolveErrorCode(Throwable throwable) {
        Class<?> current = throwable.getClass();

        while (current != null) {
            ErrorCode errorCode = exceptionMap.get(current);
            if (errorCode != null) {
                return errorCode;
            }
            current = current.getSuperclass();
        }

        return ErrorCode.INTERNAL_SERVER_ERROR;
    }
}