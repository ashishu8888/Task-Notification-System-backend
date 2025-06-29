package com.ashish.tasknotificationsystem.Exception;

import com.ashish.tasknotificationsystem.Dto.ErrorResponseDto;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGeneric(Exception e, HttpServletRequest req){
        log.error("Unexpected error: {}", e.getMessage());
        return buildResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR, req);
    }

    @ExceptionHandler(InvalidJwtException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidJwtException(InvalidJwtException e, HttpServletRequest req){
        return buildResponse(e.getMessage(),HttpStatus.UNAUTHORIZED,req);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ErrorResponseDto> handleJwtSignatureException(SignatureException e, HttpServletRequest req){
        return buildResponse(e.getMessage(),HttpStatus.UNAUTHORIZED,req);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponseDto> handleJwtException(JwtException e, HttpServletRequest req){
        return buildResponse(e.getMessage(),HttpStatus.UNAUTHORIZED,req);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> handleBadCredentials(BadCredentialsException e, HttpServletRequest req){
        log.warn("Bad credentials : {}", e.getMessage());
        return buildResponse(e.getMessage(),HttpStatus.UNAUTHORIZED, req);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUsernameNotFound(UsernameNotFoundException e, HttpServletRequest req){
        log.warn("User not found: {}", e.getMessage());
        return buildResponse(e.getMessage(),HttpStatus.NOT_FOUND,req);
    }

    private ResponseEntity<ErrorResponseDto> buildResponse(String message, HttpStatus status, HttpServletRequest req){
        return new ResponseEntity<>(new ErrorResponseDto(LocalDateTime.now(),status.value(),status.getReasonPhrase(),message,req.getRequestURI())
                ,status);
    }

}
