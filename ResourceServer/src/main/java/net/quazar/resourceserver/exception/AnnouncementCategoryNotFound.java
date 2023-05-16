package net.quazar.resourceserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AnnouncementCategoryNotFound extends RuntimeException {
    public AnnouncementCategoryNotFound() {
    }

    public AnnouncementCategoryNotFound(String message) {
        super(message);
    }

    public AnnouncementCategoryNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public AnnouncementCategoryNotFound(Throwable cause) {
        super(cause);
    }
}
