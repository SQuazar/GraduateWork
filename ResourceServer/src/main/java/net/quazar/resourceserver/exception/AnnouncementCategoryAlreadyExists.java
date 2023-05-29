package net.quazar.resourceserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AnnouncementCategoryAlreadyExists extends RuntimeException {
    public AnnouncementCategoryAlreadyExists() {
    }

    public AnnouncementCategoryAlreadyExists(String message) {
        super(message);
    }

    public AnnouncementCategoryAlreadyExists(String message, Throwable cause) {
        super(message, cause);
    }

    public AnnouncementCategoryAlreadyExists(Throwable cause) {
        super(cause);
    }
}
