package com.ip.ddangddangddang.global.exception;

import com.ip.ddangddangddang.domain.common.dto.ExceptionDto;
import com.ip.ddangddangddang.global.exception.customedExceptions.CustomUserException;
import com.ip.ddangddangddang.global.exception.customedExceptions.InvalidAuthorityException;
import com.ip.ddangddangddang.global.exception.customedExceptions.InvalidBidException;
import com.ip.ddangddangddang.global.exception.customedExceptions.TimeOutLockException;
import jakarta.persistence.EntityNotFoundException;
import java.io.FileNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j(topic = "GlobalExceptionHandler")
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TimeOutLockException.class)
    public ResponseEntity<ExceptionDto> timeOutLockException(final TimeOutLockException e) {
        log.error(e.getMessage());
        return createResponse(HttpStatus.REQUEST_TIMEOUT, e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionDto> runtimeException(final RuntimeException e) {
        log.error(e.getMessage());
        return createResponse(HttpStatus.REQUEST_TIMEOUT, e.getMessage());
    }

    @ExceptionHandler(InvalidAuthorityException.class)
    public ResponseEntity<ExceptionDto> invalidAuthorityException(
        final InvalidAuthorityException e) {
        log.error(e.getMessage());
        return createResponse(HttpStatus.REQUEST_TIMEOUT, e.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ExceptionDto> nullPointerException(final NullPointerException e) {
        log.error(e.getMessage());
        return createResponse(HttpStatus.REQUEST_TIMEOUT, e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionDto> entityNotFoundException(final EntityNotFoundException e) {
        log.error("EntityNotFoundException: ", e);
        return createResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(InvalidBidException.class)
    public ResponseEntity<ExceptionDto> invalidBidException(final InvalidBidException e) {
        log.error("CustomBidException: ", e);
        return createResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(CustomUserException.class)
    public ResponseEntity<ExceptionDto> customUserException(final CustomUserException e) {
        log.error("CustomUserException: ", e);
        return createResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ExceptionDto> fileNotFoundException(final FileNotFoundException e) {
        log.error("FileNotFoundException: ", e);
        return createResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ExceptionDto> exception(final Exception e) {
        log.error("Exception: ", e);
        return createResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    private ResponseEntity<ExceptionDto> createResponse(
        final HttpStatus status,
        final String message
    ) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new ExceptionDto(status, message));
    }

}
