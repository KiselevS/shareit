package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleBadRequestException(BadRequestException e) {
        log.info("400 {}" + e.getMessage());
        return new ErrorMessage(e.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.info("400 {}" + e.getMessage());
        return new ErrorMessage(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorMessage handleAccessDeniedException(AccessDeniedException e) {
        log.info("403 {}", e.getMessage());
        return new ErrorMessage(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleNotFoundException(NotFoundException e) {
        log.info("404 {}", e.getMessage());
        return new ErrorMessage(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage handleEmailAlreadyExistsException(ConflictException e) {
        log.info("409 {}", e.getMessage());
        return new ErrorMessage(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleThrowableExceptions(Throwable e) {
        log.info("500 {}" + e.getMessage());
        return new ErrorMessage(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleWrongStateException(WrongStateException e) {
        return new ErrorMessage(e.getMessage());
    }
}
