package com.jaeho.hello.Board.Dto.Response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jaeho.hello.Board.Common.ResponseCode;
import com.jaeho.hello.Board.Common.ResponseMessage;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto {
    
    private String code;
    private String message;

    public static ResponseEntity<ResponseDto> databaseError(){
        ResponseDto responseBody =new ResponseDto(ResponseCode.DATARBASE_ERROR,ResponseMessage.DATARBASE_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }
    public static ResponseEntity<ResponseDto> validationFailed(){
        ResponseDto responseBody =new ResponseDto(ResponseCode.VALIDATION_FAILED,ResponseMessage.VALIDATION_FAILED);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody); 
    }
}
