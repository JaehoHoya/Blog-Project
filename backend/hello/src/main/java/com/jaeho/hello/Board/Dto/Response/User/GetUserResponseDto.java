package com.jaeho.hello.Board.Dto.Response.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jaeho.hello.Board.Common.ResponseCode;
import com.jaeho.hello.Board.Common.ResponseMessage;
import com.jaeho.hello.Board.Dto.Response.ResponseDto;
import com.jaeho.hello.Board.Entity.UserEntity;

import lombok.Getter;
@Getter
public class GetUserResponseDto extends ResponseDto {
    private String email;
    private String nickname;
    private String profileImage;

    private GetUserResponseDto(UserEntity userEntity){
        super(ResponseCode.SUCCESS,ResponseMessage.SUCCESS);
        this.email=userEntity.getEmail();
        this.nickname=userEntity.getNickname();
        this.profileImage=userEntity.getProfileImage();
    }
    public static ResponseEntity<GetUserResponseDto> success(UserEntity userEntity){
        GetUserResponseDto result =new GetUserResponseDto(userEntity);
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }
    public static ResponseEntity<ResponseDto> noExistUser(){
        ResponseDto result =new ResponseDto(ResponseCode.NOT_EXISTED_USER,ResponseMessage.NOT_EXISTED_USER);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
