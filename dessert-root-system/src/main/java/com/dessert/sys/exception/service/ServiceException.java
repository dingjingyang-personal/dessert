package com.dessert.sys.exception.service;

import java.util.Map;

/**
 * Created by admin-ding on 2016/5/18.
 */
public class ServiceException extends Exception {

    private static final long serialVersionUID = -1708015121235851228L;

    private Map<String,Object> exceptionParams;

    public Map<String, Object> getExceptionParams() {
        return exceptionParams;
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message ,Map<String,Object> exceptionParams) {
        super(message);
        this.exceptionParams = exceptionParams;
    }
}
