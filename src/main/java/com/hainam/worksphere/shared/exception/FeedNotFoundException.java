package com.hainam.worksphere.shared.exception;

import org.springframework.http.HttpStatus;

public class FeedNotFoundException extends BaseException {

    private static final String ERROR_CODE = "FEED_NOT_FOUND";

    public FeedNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, ERROR_CODE);
    }

    public static FeedNotFoundException byId(String id) {
        return new FeedNotFoundException("Feed not found with id: " + id);
    }
}
