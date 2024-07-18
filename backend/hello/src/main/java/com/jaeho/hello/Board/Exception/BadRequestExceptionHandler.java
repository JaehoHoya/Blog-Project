package com.jaeho.hello.Board.Exception;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jaeho.hello.Board.Dto.Response.ResponseDto;

@RestControllerAdvice
public class BadRequestExceptionHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class,HttpMessageNotReadableException.class})
    public ResponseEntity<ResponseDto> validationExceptionHandler(Exception exception){
        return ResponseDto.validationFailed();
    }
}