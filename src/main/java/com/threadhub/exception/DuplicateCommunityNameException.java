package com.threadhub.exception;

public class DuplicateCommunityNameException extends RuntimeException {
    public DuplicateCommunityNameException(String message) {
        super(message);
    }
}
