package com.threadhub.exception;

public class OwnerCannotLeaveException extends RuntimeException {
    public OwnerCannotLeaveException(String message) {
        super(message);
    }
}
