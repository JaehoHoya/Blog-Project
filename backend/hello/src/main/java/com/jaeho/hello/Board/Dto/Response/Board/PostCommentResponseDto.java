package com.jaeho.hello.Board.Dto.Response.Board;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jaeho.hello.Board.Common.ResponseCode;
import com.jaeho.hello.Board.Common.ResponseMessage;
import com.jaeho.hello.Board.Dto.Response.ResponseDto;

import lombok.Getter;

@Getter
public class PostCommentResponseDto extends ResponseDto{
    
    private PostCommentResponseDto(){
        super(ResponseCode.SUCCESS,ResponseMessage.SUCCESS);

    }

    public static ResponseEntity<PostCommentResponseDto> success(){
        PostCommentResponseDto result = new PostCommentResponseDto();

        return ResponseEntity.status(HttpStatus.OK).body(result);

    }
    public static ResponseEntity<ResponseDto> noExistBoard(){
        ResponseDto result =new ResponseDto(ResponseCode.NOT_EXISTED_BOARD,ResponseMessage.NOT_EXISTED_BOARD);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    public static ResponseEntity<ResponseDto> noExistUser(){
        ResponseDto result =new ResponseDto(ResponseCode.NOT_EXISTED_USER,ResponseMessage.NOT_EXISTED_USER);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }

}
