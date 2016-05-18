package com.dessert.sys.exception.service;

/**
 * Created by admin-ding on 2016/5/18.
 */
public class ServiceException extends Exception {

    private static final long serialVersionUID = -1708015121235851228L;

    public ServiceException(String message) {
        super(message);
    }
}
