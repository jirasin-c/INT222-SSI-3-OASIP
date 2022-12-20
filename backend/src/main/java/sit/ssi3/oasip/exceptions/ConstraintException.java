package sit.ssi3.oasip.exceptions;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

//throw exception userService create update
public class ConstraintException extends ValidationException {
    private final Set<ConstraintViolation<?>> constraintViolations;

    public ConstraintException(String message, Set<? extends ConstraintViolation<?>> constraintViolations) {
        super(message);
        if (constraintViolations == null) {
            this.constraintViolations = null;
        } else {
            this.constraintViolations = new HashSet(constraintViolations);
        }
    }

    public ConstraintException(Set<? extends ConstraintViolation<?>> constraintViolations) {
        this(toString(constraintViolations), constraintViolations);
    }

    public Set<ConstraintViolation<?>> getConstraintViolations() {
        return this.constraintViolations;
    }

    private static String toString(Set<? extends ConstraintViolation<?>> constraintViolations) {
        return (String) constraintViolations.stream().map((cv) -> {
            return cv == null ? "null" : (cv.getPropertyPath().toString().equals("isEmailUnique") ? "email" :  cv.getPropertyPath().toString().equals("isNameUnique") ? "name" : cv.getPropertyPath().toString().equals("isRoleSpecified") ? "role" : cv.getPropertyPath().toString().equals("isPasswordFit") ? "password" : cv.getPropertyPath()) + ": " + cv.getMessage();
        }).collect(Collectors.joining(", "));
    }}