package sit.ssi3.oasip.storage;

import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//throw spring.servlet.multipart.max-file-size=10MB , spring.servlet.multipart.max-request-size=20MB
@ControllerAdvice
public class FileUploadExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new String("The file size cannot be larger than 10 MB"));
    }
}