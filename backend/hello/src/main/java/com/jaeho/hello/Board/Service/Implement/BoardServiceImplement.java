package com.jaeho.hello.Board.Service.Implement;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jaeho.hello.Board.Dto.Requset.Board.PatchBoardRquestDto;
import com.jaeho.hello.Board.Dto.Requset.Board.PostBoardRequestDto;
import com.jaeho.hello.Board.Dto.Requset.Board.PostCommentRequestDto;
import com.jaeho.hello.Board.Dto.Response.ResponseDto;
import com.jaeho.hello.Board.Dto.Response.Board.DeleteBoardResponseDto;
import com.jaeho.hello.Board.Dto.Response.Board.GetBoardResponseDto;
import com.jaeho.hello.Board.Dto.Response.Board.GetComentListResponseDto;
import com.jaeho.hello.Board.Dto.Response.Board.GetFavoriteListResponseDto;
import com.jaeho.hello.Board.Dto.Response.Board.GetLatestBoardListResponseDto;
import com.jaeho.hello.Board.Dto.Response.Board.GetSearchBoardResponseDto;
import com.jaeho.hello.Board.Dto.Response.Board.GetTop3BoardListResponseDto;
import com.jaeho.hello.Board.Dto.Response.Board.GetUserBoardListResponseDto;
import com.jaeho.hello.Board.Dto.Response.Board.IncreaseViewCountResponseDto;
import com.jaeho.hello.Board.Dto.Response.Board.PatchBoardResponseDto;
import com.jaeho.hello.Board.Dto.Response.Board.PostBoardResponseDto;
import com.jaeho.hello.Board.Dto.Response.Board.PostCommentResponseDto;
import com.jaeho.hello.Board.Dto.Response.Board.PutFavoriteResponseDto;
import com.jaeho.hello.Board.Entity.BoardEntity;
import com.jaeho.hello.Board.Entity.BoardListViewEntity;
import com.jaeho.hello.Board.Entity.CommentEntity;
import com.jaeho.hello.Board.Entity.FavoriteEntity;
import com.jaeho.hello.Board.Entity.ImageEntity;
import com.jaeho.hello.Board.Entity.SearchLogEntity;
import com.jaeho.hello.Board.Repository.BoardListViewRepository;
import com.jaeho.hello.Board.Repository.BoardRepository;
import com.jaeho.hello.Board.Repository.CommentRepository;
import com.jaeho.hello.Board.Repository.FavoriteRepository;
import com.jaeho.hello.Board.Repository.ImageRepository;
import com.jaeho.hello.Board.Repository.SearchLogRepository;
import com.jaeho.hello.Board.Repository.UserRepository;
import com.jaeho.hello.Board.Repository.resultSet.GetBoardResultSet;
import com.jaeho.hello.Board.Repository.resultSet.GetCommentListResultSet;
import com.jaeho.hello.Board.Repository.resultSet.GetFavoriteListResultSet;
import com.jaeho.hello.Board.Service.BoardService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImplement implements BoardService{

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final CommentRepository commentRepository;
    private final FavoriteRepository favoriteRepository;
    private final BoardListViewRepository boardListViewRepository;
    private final SearchLogRepository searchLogRepository;

    @Override
    public ResponseEntity<? super GetBoardResponseDto> getBoard(Integer boardNumber) {
        
        GetBoardResultSet resultSet=null;
        List<ImageEntity> imageEntities= new ArrayList<>();
        try {
            
             resultSet =boardRepository.getBoard(boardNumber);
            if(resultSet==null) return GetBoardResponseDto.noExistBoard();

            imageEntities =imageRepository.findByBoardNumber(boardNumber);

            

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }




        return GetBoardResponseDto.success(resultSet,imageEntities);




    }
   
    @Override
    public ResponseEntity<? super GetFavoriteListResponseDto> getFavoriteList(Integer boardNumber) {

            List<GetFavoriteListResultSet> resultSets =new ArrayList<>();
            try {
                
                boolean existedBoard =boardRepository.existsByBoardNumber(boardNumber);
                if(!existedBoard) return GetFavoriteListResponseDto.noExistBoard();
                
                resultSets=favoriteRepository.getFavoriteList(boardNumber);




            } catch (Exception exception) {
                exception.printStackTrace();
                return ResponseDto.databaseError();
            }


        return GetFavoriteListResponseDto.success(resultSets);

    }
    
    @Override
    public ResponseEntity<? super GetComentListResponseDto> getCommentList(Integer boardNumber) {
        
        List<GetCommentListResultSet> resultSets =new ArrayList<>();

        try {
            
            boolean existedBoard =boardRepository.existsByBoardNumber(boardNumber);
            if(!existedBoard) return GetComentListResponseDto.noExistBoard();

            resultSets =commentRepository.getCommentList(boardNumber);



        } catch (Exception exception) {
           exception.printStackTrace();
           
        }
        return GetComentListResponseDto.success(resultSets);
    }
   
    @Override
    public ResponseEntity<? super GetLatestBoardListResponseDto> getLatestBoardList() {
        
        List<BoardListViewEntity> boardListViewEntities =new ArrayList<>();


        try {
            
            boardListViewEntities = boardListViewRepository.findByOrderByWriteDatetimeDesc();



        } catch (Exception exception) {
           exception.printStackTrace();
           return ResponseDto.databaseError();
        }


        return GetLatestBoardListResponseDto.success(boardListViewEntities);
    }

    @Override
    public ResponseEntity<? super GetTop3BoardListResponseDto> getTop3BoardList() {
        
        List<BoardListViewEntity> boardListViewEntities =new ArrayList<>();


        try {

            Date beforeWeek= Date.from(Instant.now().minus(7,ChronoUnit.DAYS));
            SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String sevenDaysAgo =simpleDateFormat.format(beforeWeek);
            boardListViewEntities =boardListViewRepository.findTop3ByWriteDatetimeGreaterThanOrderByFavoriteCountDescCommentCountDescViewCountDescWriteDatetimeDesc(sevenDaysAgo);

            
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return GetTop3BoardListResponseDto.success(boardListViewEntities);
    }
    @Override
    public ResponseEntity<? super GetSearchBoardResponseDto> getSearchBoardList(String searchWord, String preSearchWord) {
        
        List<BoardListViewEntity> boardListViewEntities =new ArrayList<>();

        try {
            
            boardListViewEntities =boardListViewRepository.findByTitleContainsOrContentContainsOrderByWriteDatetimeDesc(searchWord, searchWord);
        
            SearchLogEntity searchLogEntity =new SearchLogEntity(searchWord,preSearchWord,false);
            searchLogRepository.save(searchLogEntity);
            boolean relation = preSearchWord !=null;
            if(relation){
                searchLogEntity= new SearchLogEntity(preSearchWord,searchWord,true);
                searchLogRepository.save(searchLogEntity);
            }



        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetSearchBoardResponseDto.success(boardListViewEntities);


    }

    @Override
    public ResponseEntity<? super GetUserBoardListResponseDto> getUserBoardList(String email) {
      
        List<BoardListViewEntity> boardListViewEntities =new ArrayList<>();

      
        try {

            boolean existedUser =userRepository.existsByEmail(email);
            if(!existedUser) return GetUserBoardListResponseDto.noExistUser();

            boardListViewEntities =boardListViewRepository.findByWriterEmailOrderByWriteDatetimeDesc(email);
            
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetUserBoardListResponseDto.success(boardListViewEntities);


    }






    @Override
    public ResponseEntity<? super PostBoardResponseDto> postBoard(PostBoardRequestDto dto, String email) {
       
        try {

            boolean existedEmail =userRepository.existsByEmail(email);
            if(!existedEmail) return PostBoardResponseDto.notExistUser();

            BoardEntity boardEntity =new BoardEntity(dto,email);
            boardRepository.save(boardEntity);

            int boardNumaber =boardEntity.getBoardNumber();
            List<String> boardImageList =dto.getBoardImageList();
            List<ImageEntity> imageEntities =new ArrayList<>();

            for(String image: boardImageList){
                ImageEntity imageEntity =new ImageEntity(boardNumaber, image);
                imageEntities.add(imageEntity);
            }

            imageRepository.saveAll(imageEntities);

            
        } catch (Exception exception) {
            exception.printStackTrace();

            return ResponseDto.databaseError();
        }


        return PostBoardResponseDto.success();


    }

    @Override
    public ResponseEntity<? super PostCommentResponseDto> postComment(PostCommentRequestDto dto,Integer boardNumber, String email) {
        

        try {
            BoardEntity boardEntity = boardRepository.findByBoardNumber(boardNumber);
            if(boardEntity ==null) return PostCommentResponseDto.noExistBoard();

            boolean existedUser =userRepository.existsByEmail(email);
            if(!existedUser) return PostCommentResponseDto.noExistUser();

            CommentEntity commentEntity =new CommentEntity(dto,boardNumber, email);
            commentRepository.save(commentEntity);

            boardEntity.increaseCommentCount();
            boardRepository.save(boardEntity);


        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return PostCommentResponseDto.success();







    }

    @Override
    public ResponseEntity<? super PutFavoriteResponseDto> putFavorite(Integer boardNumber, String email) {
       
        try {

            boolean existedUser =userRepository.existsByEmail(email);
            if(!existedUser) return PutFavoriteResponseDto.noExistUser();

            BoardEntity boardEntity =boardRepository.findByBoardNumber(boardNumber);
            if(boardEntity==null ) return PutFavoriteResponseDto.noExistBoard();

            FavoriteEntity favoriteEntity= favoriteRepository.findByBoardNumberAndUserEmail(boardNumber, email);
            if(favoriteEntity==null){
                favoriteEntity= new FavoriteEntity(email,boardNumber);
                favoriteRepository.save(favoriteEntity);
                boardEntity.increaseFavoriteCount();
            }
            else{
                favoriteRepository.delete(favoriteEntity);
                boardEntity.decreaseFavoriteCount();
            }
            boardRepository.save(boardEntity);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return PutFavoriteResponseDto.success();
    }
    
    @Override
    public ResponseEntity<? super PatchBoardResponseDto> patchBoard(PatchBoardRquestDto dto, Integer boardNumber,String email) {

        try {

            BoardEntity boardEntity = boardRepository.findByBoardNumber(boardNumber);
            if(boardEntity ==null) return PatchBoardResponseDto.noExistBoard();

            boolean existedUser =userRepository.existsByEmail(email);
            if(!existedUser) return PatchBoardResponseDto.noExistUser();

            String wirterEmail =boardEntity.getWriterEmail();
            boolean isWriter = wirterEmail.equals(email);
            if(!isWriter) return PatchBoardResponseDto.noPermisson();


            boardEntity.patchBoard(dto);
            boardRepository.save(boardEntity);

            imageRepository.deleteByBoardNumber(boardNumber);
            List<String> boardImageList =dto.getBoardImageList();

            List<ImageEntity> imageEntities=new ArrayList<>();


            for(String image:boardImageList){
                ImageEntity imageEntity =new ImageEntity(boardNumber,image);
                imageEntities.add(imageEntity);
            }

            imageRepository.saveAll(imageEntities);
             
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
           
        }
        return PatchBoardResponseDto.success();
    }

    @Override
    public ResponseEntity<? super IncreaseViewCountResponseDto> increaseViewCount(Integer boardNumber) {
        try {
            
            BoardEntity boardEntity =boardRepository.findByBoardNumber(boardNumber);
            
            if(boardEntity==null)return IncreaseViewCountResponseDto.noExistBoard();
            boardEntity.increaseViewCount();
            boardRepository.save(boardEntity);

        } catch (Exception exception) {
           exception.printStackTrace();
           return ResponseDto.databaseError();
        }
    
        return IncreaseViewCountResponseDto.success();
    }

    @Override
    public ResponseEntity<? super DeleteBoardResponseDto> deleteBoard(Integer boardNumber, String email) {
        
        try {
            boolean existedUser =userRepository.existsByEmail(email);
            if(!existedUser) return DeleteBoardResponseDto.noExistUser();

            BoardEntity boardEntity = boardRepository.findByBoardNumber(boardNumber);
            if(boardEntity==null) return DeleteBoardResponseDto.noExistBoard();

            String writerEmail= boardEntity.getWriterEmail();
            boolean isWriter =writerEmail.equals(email);

            if(!isWriter) return DeleteBoardResponseDto.noPermission();

            imageRepository.deleteByBoardNumber(boardNumber);
            commentRepository.deleteByBoardNumber(boardNumber);
            favoriteRepository.deleteByBoardNumber(boardNumber);

            boardRepository.delete(boardEntity);
            

            
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return DeleteBoardResponseDto.success();
    }

}
