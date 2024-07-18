package com.jaeho.hello.Board.Service.Implement;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jaeho.hello.Board.Dto.Requset.Auth.SignInRequestDto;
import com.jaeho.hello.Board.Dto.Requset.Auth.SignUpRequestDto;
import com.jaeho.hello.Board.Dto.Response.ResponseDto;
import com.jaeho.hello.Board.Dto.Response.Auth.SignInResponseDto;
import com.jaeho.hello.Board.Dto.Response.Auth.SignUpResponseDto;
import com.jaeho.hello.Board.Entity.UserEntity;
import com.jaeho.hello.Board.Provider.JwtProvider;
import com.jaeho.hello.Board.Repository.UserRepository;
import com.jaeho.hello.Board.Service.AuthService;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class AuthServiceImplement implements AuthService{

    
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private PasswordEncoder passwordEncoder =new BCryptPasswordEncoder();
    
    @Override
    public ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto) {
        

        try {
        //
        String email= dto.getEmail();
        boolean existedEmail=userRepository.existsByEmail(email);
        if(existedEmail) return SignUpResponseDto.duplicateEmail();

        //
        String nickname=dto.getNickname();
        boolean existedNickname=userRepository.existsByNickname(nickname);
        if(existedNickname) return SignUpResponseDto.duplicateNickname();

        //
        String telNumber=dto.getTelNumber();
        boolean existedTelNumber=userRepository.existsByTelNumber(telNumber);
        if(existedTelNumber) return SignUpResponseDto.duplicateTelNumber();

        String password =dto.getPassword();
        String encodedPassword=passwordEncoder.encode(password);
            dto.setPassword(encodedPassword);

        UserEntity userEntity =new UserEntity(dto); 
        userRepository.save(userEntity);


        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return SignUpResponseDto.success();




    }

    @Override
    public ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto) {
        
        String token =null;
        
    try {
        //
        String email=dto.getEmail();
        UserEntity userEntity= userRepository.findByEmail(email);
            if(userEntity==null) return SignInResponseDto.signInFaild();
        //
        String password =dto.getPassword();
        String encodedPassword=userEntity.getPassword();
        boolean isMatched =passwordEncoder.matches(password,encodedPassword);
            if(! isMatched) return SignInResponseDto.signInFaild();

        token=jwtProvider.create(email);


    } catch (Exception exception) {

        exception.printStackTrace();
        return ResponseDto.databaseError();
    }


    return SignInResponseDto.success(token);






   }
}