package com.server.Puzzle.domain.board.service.Impl;

import com.server.Puzzle.domain.board.domain.Board;
import com.server.Puzzle.domain.board.domain.BoardField;
import com.server.Puzzle.domain.board.domain.BoardFile;
import com.server.Puzzle.domain.board.domain.BoardLanguage;
import com.server.Puzzle.domain.board.dto.request.CorrectionPostRequestDto;
import com.server.Puzzle.domain.board.dto.request.PostRequestDto;
import com.server.Puzzle.domain.board.dto.response.GetAllPostResponseDto;
import com.server.Puzzle.domain.board.dto.response.GetPostByTagResponseDto;
import com.server.Puzzle.domain.board.dto.response.GetPostResponseDto;
import com.server.Puzzle.domain.board.enumType.Purpose;
import com.server.Puzzle.domain.board.enumType.Status;
import com.server.Puzzle.domain.board.repository.BoardFieldRepository;
import com.server.Puzzle.domain.board.repository.BoardFileRepository;
import com.server.Puzzle.domain.board.repository.BoardLanguageRepository;
import com.server.Puzzle.domain.board.repository.BoardRepository;
import com.server.Puzzle.domain.board.service.BoardService;
import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.global.enumType.Field;
import com.server.Puzzle.global.enumType.Language;
import com.server.Puzzle.global.util.AwsS3Util;
import com.server.Puzzle.global.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {

    private final CurrentUserUtil currentUserUtil;
    private final AwsS3Util awsS3Util;

    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    private final BoardFieldRepository boardFieldRepository;
    private final BoardLanguageRepository boardLanguageRepository;

    @Override
    public Board post(PostRequestDto request) {
        String title = request.getTitle();
        String contents = request.getContents();
        Purpose purpose = request.getPurpose();
        Status status = request.getStatus();
        List<Field> fieldList = request.getFieldList();
        List<Language> languageList = request.getLanguageList();
        List<String> fileUrlList = request.getFileUrlList();
        User currentUser = currentUserUtil.getCurrentUser();

        Board board = boardRepository.save(
                Board.builder()
                        .title(title)
                        .contents(contents)
                        .purpose(purpose)
                        .status(status)
                        .user(currentUser)
                        .build()
        );

        for (String fileUrl : fileUrlList) {
            boardFileRepository.save(
                    BoardFile.builder()
                            .board(board)
                            .url(fileUrl)
                            .build()
            );
        }

        for (Field field : fieldList) {
            boardFieldRepository.save(
                    BoardField.builder()
                            .board(board)
                            .field(field)
                            .build()
            );
        }

        for (Language language : languageList) {
            boardLanguageRepository.save(
                    BoardLanguage.builder()
                            .board(board)
                            .language(language)
                            .build()
            );
        }

        return board;
    }

    @Override
    public String createUrl(MultipartFile files) {
        String filename = awsS3Util.putS3(files);

        return "https://springbootpuzzletest.s3.ap-northeast-2.amazonaws.com/"+filename;
    }

    @Transactional
    @Override
    public Board correctionPost(Long id, CorrectionPostRequestDto request) {
        String title = request.getTitle();
        String contents = request.getContents();
        Purpose purpose = request.getPurpose();
        Status status = request.getStatus();
        List<Field> fieldList = request.getFieldList();
        List<Language> languageList = request.getLanguageList();

        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));
        if(board.getUser() != currentUserUtil.getCurrentUser()) throw new IllegalArgumentException("게시물을 수정할 권한이 없습니다.");

        board
                .updateTitle(title)
                .updateContents(contents)
                .updatePurpose(purpose)
                .updateStatus(status);

        boardFieldRepository.deleteByBoardId(board.getId());
        boardLanguageRepository.deleteByBoardId(board.getId());

        for (Field field : fieldList) {
            boardFieldRepository.save(
                    BoardField.builder()
                            .board(board)
                            .field(field)
                            .build()
            );
        }

        for (Language language : languageList) {
            boardLanguageRepository.save(
                    BoardLanguage.builder()
                            .board(board)
                            .language(language)
                            .build()
            );
        }

        List<String> saveFileUrlList = this.getSaveFileUrlList(board, request);

        try{
            for (String fileUrl : saveFileUrlList) {
                boardFileRepository.save(
                        BoardFile.builder()
                                .board(board)
                                .url(fileUrl)
                                .build()
                );
            }
        } catch (NullPointerException e){
            return board;
        }

        return board;
    }

    @Override
    public Page<GetAllPostResponseDto> getAllPost(Pageable pageable) {
        Page<GetAllPostResponseDto> response = boardRepository.findAll(pageable).map(
                board -> GetAllPostResponseDto.builder()
                        .title(board.getTitle())
                        .status(board.getStatus())
                        .createDateTime(board.getCreatedDate())
                        .image_url(
                                board.getBoardFiles().stream()
                                        .map(boardFile -> boardFile.getUrl())
                                        .findFirst().orElse(null)
                        )
                        .build()
        );

        return response;
    }

    @Override
    public GetPostResponseDto getPost(Long id) {
        GetPostResponseDto response = boardRepository.findById(id).map(
                board -> GetPostResponseDto.builder()
                        .id(id)
                        .title(board.getTitle())
                        .contents(board.getContents())
                        .purpose(board.getPurpose())
                        .status(board.getStatus())
                        .fields(
                                board.getBoardFields().stream()
                                .map(boardField -> boardField.getField())
                                .collect(Collectors.toList())
                        )
                        .languages(
                                board.getBoardLanguages().stream()
                                .map(boardLanguage -> boardLanguage.getLanguage())
                                .collect(Collectors.toList())
                        )
                        .files(
                                board.getBoardFiles().stream()
                                .map(boardFile -> boardFile.getUrl())
                                .collect(Collectors.toList())
                        )
                        .build()
        ).orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

        return response;
    }

    @Override
    public void deletePost(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

        if(board.getUser() == currentUserUtil.getCurrentUser()){
            List<BoardFile> boardFiles = board.getBoardFiles();
            for (BoardFile boardFile : boardFiles) {
                awsS3Util.deleteS3(boardFile.getUrl().substring(61));
            }
            boardRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("게시물을 삭제할 수 있는 권한이 없습니다.");
        }
    }

    @Override
    public List<GetPostByTagResponseDto> getPostByTag(Purpose purpose, List<Field> field, List<Language> language, Status status,Pageable pageable) {
        List<GetPostByTagResponseDto> response = boardRepository.findBoardByTag(purpose, field, language, status, pageable);

        return response;
    }

    private List<String> getSaveFileUrlList(Board board, CorrectionPostRequestDto request){
        List<BoardFile> dbBoardFileList = boardFileRepository.findByBoardId(board.getId());
        List<String> requestFileUrlList = request.getFileUrlList();
        List<String> addFileList = new ArrayList<>();

        if(CollectionUtils.isEmpty(dbBoardFileList)) { // db에 url 이 없다면,
            if(!CollectionUtils.isEmpty(requestFileUrlList)) { // 요청파일 목록에 파일이 있다면,
                for (String fileUrls : requestFileUrlList) {
                    addFileList.add(fileUrls); // addFileList 에 fileUrls 을 추가
                }
                return addFileList;
            } else {
                return null;
            }
        } else { // db에 url 이 있다면,
            if(CollectionUtils.isEmpty(requestFileUrlList)){ // 요청파일 목록에 파일이 없다면,
                for (BoardFile dbBoardFile : dbBoardFileList) {
                    boardFileRepository.deleteById(dbBoardFile.getId()); // BoardFile 테이블에서 해당 게시물의 url들을 삭제
                    awsS3Util.deleteS3(dbBoardFile.getUrl().substring(61));
                }
            } else { // 요청파일 목록에 파일이 있다면,
                List<String> dbBoardFileUrlList = new ArrayList<>();

                for (BoardFile dbBoardFile : dbBoardFileList) {
                    if(!requestFileUrlList.contains(dbBoardFile.getUrl())){ // 요청파일 목록에 이미 db에 존재하는 파일이 존재하지 않는다면,
                        boardFileRepository.deleteById(dbBoardFile.getId()); // 해당 데이터 삭제
                        awsS3Util.deleteS3(dbBoardFile.getUrl().substring(61));
                    } else { // 요청 파일 목록에 이미 db에 존재하는 파일이 존재한다면,
                        dbBoardFileUrlList.add(dbBoardFile.getUrl());
                    }
                }

                for (String requestFileUrl : requestFileUrlList) {
                    if(!dbBoardFileUrlList.contains(requestFileUrl))
                        addFileList.add(requestFileUrl);
                }

                return addFileList;
            }
        }

        return null;
    }

}
