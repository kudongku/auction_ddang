package com.ip.ddangddangddang.global.exception;

import com.ip.ddangddangddang.domain.common.dto.ExceptionDto;
import com.ip.ddangddangddang.global.exception.custom.AuctionNotFoundException;
import com.ip.ddangddangddang.global.exception.custom.CustomAuctionException;
import com.ip.ddangddangddang.global.exception.custom.CustomBidException;
import com.ip.ddangddangddang.global.exception.custom.CustomCommentException;
import com.ip.ddangddangddang.global.exception.custom.CustomResultException;
import com.ip.ddangddangddang.global.exception.custom.CustomTownException;
import com.ip.ddangddangddang.global.exception.custom.CustomUserException;
import com.ip.ddangddangddang.global.exception.custom.LockNotAcquiredException;
import com.ip.ddangddangddang.global.exception.custom.TimeOutLockException;
import com.ip.ddangddangddang.global.exception.custom.UserHasNotAuthorityToAuctionException;
import com.ip.ddangddangddang.global.exception.custom.UserHasNotAuthorityToFileException;
import com.ip.ddangddangddang.global.exception.custom.UserNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import java.io.FileNotFoundException;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionDto> userNotFoundException(final UserNotFoundException e) {
        log.error("UserNotFoundException: ", e);
        return createResponse(HttpStatus.BAD_REQUEST, "UserNotFoundException: " + e.getMessage());
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ExceptionDto> fileNotFoundException(final FileNotFoundException e) {
        log.error("FileNotFoundException: ", e);
        return createResponse(HttpStatus.BAD_REQUEST, "FileNotFoundException: " + e.getMessage());
    }

    @ExceptionHandler(UserHasNotAuthorityToFileException.class)
    public ResponseEntity<ExceptionDto> userHasNoAuthorityException(
        final UserHasNotAuthorityToFileException e) {
        log.error("UserHasNoAuthorityException: ", e);
        return createResponse(HttpStatus.BAD_REQUEST,
            "UserHasNoAuthorityException: " + e.getMessage());
    }

    @ExceptionHandler(UserHasNotAuthorityToAuctionException.class)
    public ResponseEntity<ExceptionDto> userHasNotAuthorityToAuctionException(
        final UserHasNotAuthorityToAuctionException e) {
        log.error("UserHasNotAuthorityToAuctionException: ", e);
        return createResponse(HttpStatus.BAD_REQUEST,
            "UserHasNotAuthorityToAuctionException: " + e.getMessage());
    }

    @ExceptionHandler(AuctionNotFoundException.class)
    public ResponseEntity<ExceptionDto> auctionNotFoundException(final AuctionNotFoundException e) {
        log.error("AuctionNotFoundException: ", e);
        return createResponse(HttpStatus.BAD_REQUEST,
            "AuctionNotFoundException: " + e.getMessage());
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
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new ExceptionDto(status, message));
    }

}
