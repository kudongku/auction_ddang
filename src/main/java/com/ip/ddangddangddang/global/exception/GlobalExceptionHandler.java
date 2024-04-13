package com.ip.ddangddangddang.global.exception;

import com.ip.ddangddangddang.domain.common.dto.ExceptionDto;
import com.ip.ddangddangddang.global.exception.custom.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@Slf4j(topic = "GlobalExceptionHandler")
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TimeOutLockException.class)
    public ResponseEntity<ExceptionDto> timeOutLockException(final TimeOutLockException e) {
        log.error("TimeOutLockException: ", e);
        return createResponse(HttpStatus.REQUEST_TIMEOUT, "Lock Exception: " + e.getMessage());
    }

    @ExceptionHandler(LockNotAcquiredException.class)
    public ResponseEntity<ExceptionDto> lockNotAcquiredException(final LockNotAcquiredException e) {
        log.error("LockNotAcquiredException: ", e);
        return createResponse(HttpStatus.REQUEST_TIMEOUT, "Lock Exception: " + e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionDto> entityNotFoundException(final EntityNotFoundException e) {
        log.error("EntityNotFoundException: ", e);
        return createResponse(HttpStatus.BAD_REQUEST, "EntityNotFoundException: " + e.getMessage());
    }

    @ExceptionHandler(CustomAuctionException.class)
    public ResponseEntity<ExceptionDto> customAuctionException(final CustomAuctionException e) {
        log.error("CustomAuctionException: ", e);
        return createResponse(HttpStatus.BAD_REQUEST,
            "CustomAuctionException: " + e.getMessage());
    }

    @ExceptionHandler(CustomBidException.class)
    public ResponseEntity<ExceptionDto> customBidException(final CustomBidException e) {
        log.error("CustomBidException: ", e);
        return createResponse(HttpStatus.BAD_REQUEST, "CustomBidException: " + e.getMessage());
    }

    @ExceptionHandler(CustomCommentException.class)
    public ResponseEntity<ExceptionDto> customCommentException(final CustomCommentException e) {
        log.error("CustomCommentException: ", e);
        return createResponse(HttpStatus.BAD_REQUEST, "CustomCommentException: " + e.getMessage());
    }

    @ExceptionHandler(CustomResultException.class)
    public ResponseEntity<ExceptionDto> customResultException(final CustomResultException e) {
        log.error("CustomResultException: ", e);
        return createResponse(HttpStatus.BAD_REQUEST, "CustomResultException: " + e.getMessage());
    }

    @ExceptionHandler(CustomTownException.class)
    public ResponseEntity<ExceptionDto> customTownException(final CustomTownException e) {
        log.error("CustomTownException: ", e);
        return createResponse(HttpStatus.BAD_REQUEST, "CustomTownException: " + e.getMessage());
    }

    @ExceptionHandler(CustomUserException.class)
    public ResponseEntity<ExceptionDto> customUserException(final CustomUserException e) {
        log.error("CustomUserException: ", e);
        return createResponse(HttpStatus.BAD_REQUEST, "CustomUserException: " + e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDto> handleMethodArgumentNotValidException(
        final MethodArgumentNotValidException e
    ) {
        log.error("MethodArgumentNotValidException: ", e);
        return createResponse(
            HttpStatus.OK,
            Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage()
        );
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ExceptionDto> exception(final Exception e) {
        log.error("Exception: ", e);
        return createResponse(HttpStatus.I_AM_A_TEAPOT, "Unknown Error: " + e.getMessage());
    }

    private ResponseEntity<ExceptionDto> createResponse(
        final HttpStatus status,
        final String message
    ) {
        return ResponseEntity.status(status.value())
            .body(new ExceptionDto(message));
    }

}
