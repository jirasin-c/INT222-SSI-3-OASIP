package sit.ssi3.oasip.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

//ValidationHandler validate Role accessได้มั้ย eventService userService
@ControllerAdvice
public class ValidationHandler {

        @ExceptionHandler(ConstraintException.class)
        public void handleConstraintViolationException(ConstraintException exception,
                                                       ServletWebRequest webRequest) throws IOException {
            webRequest.getResponse().sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
        }

    public static ResponseEntity<Object> showError(HttpStatus status, String message) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("TIMESTAMP", Instant.now());
        errors.put("status", status.value());
        errors.put("message", message);

        return new ResponseEntity<Object>(errors, status);
    }

    }

