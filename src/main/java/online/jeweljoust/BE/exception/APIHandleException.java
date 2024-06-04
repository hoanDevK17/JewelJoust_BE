package online.jeweljoust.BE.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice

public class APIHandleException {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleInvalidUserNamePassword(BadCredentialsException ex) {
        return new ResponseEntity<>("Username or password not correct!!!", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationServiceException.class)
    public ResponseEntity<String> handleAuthenticationServiceException(AuthenticationServiceException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAuthenticationServiceException(AccessDeniedException e) {
        return new ResponseEntity<>("No access to register", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Object> handleSQLIntegrityConstraintViolation(SQLIntegrityConstraintViolationException ex) {
        String message = ex.getMessage();
        if (message.contains("account.UK_gex1lmaqpg0ir5g1f5eftyaa1")){
            return new ResponseEntity<>("Duplicate username!!!", HttpStatus.BAD_REQUEST);
        } else if (message.contains("account.UK_q0uja26qgu1atulenwup9rxyr")){
            return new ResponseEntity<>("Duplicate email!!!", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>("Database error!!!", HttpStatus.BAD_REQUEST);
        }
    }

//    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
//    public ResponseEntity<Object> handleDuplicatePhone(SQLIntegrityConstraintViolationException ex) {
//        return new ResponseEntity<>("Duplicate phone number!!!", HttpStatus.BAD_REQUEST);
//    }

//    @ExceptionHandler(AuthException.class)
//    public ResponseEntity<Object> handleDuplicatePhone(AuthException ex) {
//        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
//    }

}
