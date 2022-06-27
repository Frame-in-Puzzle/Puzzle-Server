package com.server.Puzzle.domain.board.service.Impl;

import com.server.Puzzle.domain.board.domain.Board;
import com.server.Puzzle.domain.board.domain.BoardField;
import com.server.Puzzle.domain.board.domain.BoardImage;
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
import com.server.Puzzle.global.enumType.Field;
import com.server.Puzzle.global.enumType.Language;
import com.server.Puzzle.global.exception.CustomException;
import com.server.Puzzle.global.util.AwsS3Util;
import com.server.Puzzle.global.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static com.server.Puzzle.global.exception.ErrorCode.*;

/**
 * BoardServiceImpl <br>
 * Puzzle 게시글 서비스 로직
 */
@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {

    @Value("${cloud.aws.url}")
    private String s3Url;

    private final CurrentUserUtil currentUserUtil;
    private final AwsS3Util awsS3Util;

    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    private final BoardFieldRepository boardFieldRepository;
    private final BoardLanguageRepository boardLanguageRepository;

    /**
     * 게시글을 저장하는 서비스로직
     * @param request title, contents, purpose, status, introduce, fieldList, languageList, fileUrlList
     */
    @Override
    public void post(PostRequestDto request) {
        Board board = boardRepository.save(
                request.dtoToEntity(currentUserUtil.getCurrentUser())
        );

        saveFiledUrls(request.getImageUrls(), board);
        saveFileds(request.getFields(), board);
        saveLanguages(request.getLanguages(), board);
    }

    /**
     * 이미지 url을 생성하는 서비스 로직
     * @param image
     * @return s3Url + filename
     */
    @Override
    public String createUrl(MultipartFile image) {
        String filename = awsS3Util.putS3(image);

        return s3Url + filename;
    }

    /**
     * 게시글을 수정하는 서비스 로직
     * @param boardId
     * @param request title, contents, purpose, status, introduce, fileUrlList, fieldList, languageList
     */
    @Transactional
    @Override
    public void correctionPost(Long boardId, CorrectionPostRequestDto request) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(BOARD_NOT_FOUND));
        if(!board.isAuthor(currentUserUtil.getCurrentUser())) throw new CustomException(BOARD_NOT_HAVE_PERMISSION);

        board
                .updateTitle(request.getTitle())
                .updateContents(request.getContents())
                .updatePurpose(request.getPurpose())
                .updateStatus(request.getStatus())
                .updateIntroduce(request.getIntroduce());

        boardFieldRepository.deleteByBoardId(board.getId());
        boardLanguageRepository.deleteByBoardId(board.getId());

        saveFileds(request.getFields(), board);
        saveLanguages(request.getLanguages(), board);

        List<String> saveFileUrlList = this.getSaveImageUrls(board, request);

        if(saveFileUrlList == Collections.EMPTY_LIST) saveFiledUrls(saveFileUrlList, board);
    }

    /**
     * 게시글을 전체조회하는 서비스 로직
     * @param pageable
     * @return List GetAllPostResponseDto - boardId, title, status, createDateTime, image_url, introduce
     */
    @Override
    public Page<GetAllPostResponseDto> getAllPost(Pageable pageable) {
        return boardRepository.findAll(pageable).map(
                board -> GetAllPostResponseDto.builder()
                        .boardId(board.getId())
                        .title(board.getTitle())
                        .status(board.getStatus())
                        .createDateTime(board.getCreatedDate())
                        .thumbnail(
                                board.getBoardImages().stream()
                                        .map(BoardImage::getImageUrl)
                                        .findFirst().orElse(null)
                        )
                        .build()
        );
    }

    /**
     * 게시글을 세부조회하는 서비스 로직
     * @param id
     * @return GetPostResponseDto - id, title, contents, purpose, status, name, githubId, introduce, createDateTime, fields, languages, files
     */
    @Override
    public GetPostResponseDto getPost(Long id) {
        return boardRepository.findById(id).map(
                board -> GetPostResponseDto.builder()
                        .id(id)
                        .title(board.getTitle())
                        .contents(board.getContents())
                        .purpose(board.getPurpose())
                        .status(board.getStatus())
                        .name(board.getUser().getName())
                        .githubId(board.getUser().getGithubId())
                        .createdAt(board.getCreatedDate())
                        .introduce(board.getIntroduce())
                        .fields(
                                board.getBoardFields().stream()
                                .map(BoardField::getField)
                                .collect(Collectors.toList())
                        )
                        .languages(
                                board.getBoardLanguages().stream()
                                .map(BoardLanguage::getLanguage)
                                .collect(Collectors.toList())
                        )
                        .imageUrls(
                                board.getBoardImages().stream()
                                .map(BoardImage::getImageUrl)
                                .collect(Collectors.toList())
                        )
                        .build()
        ).orElseThrow(() -> new CustomException(BOARD_NOT_FOUND));
    }

    /**
     * 게시글을 삭제하는 서비스 로직
     * @param id
     */
    @Override
    public void deletePost(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(BOARD_NOT_FOUND));

        if(board.isAuthor(currentUserUtil.getCurrentUser())){
            List<BoardImage> boardImageList = board.getBoardImages();
            for (BoardImage boardImage : boardImageList) awsS3Util.deleteS3(boardImage.getImageUrl().substring(61));
            boardRepository.deleteById(id);
        } else {
            throw new CustomException(BOARD_NOT_HAVE_PERMISSION);
        }
    }

    /**
     * 게시글을 태그조회하는 서비스로직
     * @param purpose
     * @param fields
     * @param languages
     * @param status
     * @param pageable
     * @return Page GetPostByTagResponseDto - boardId, title, status, createdDate, fileUrl, introduce
     */
    @Override
    public Page<GetPostByTagResponseDto> getPostByTag(Purpose purpose, List<Field> fields, List<Language> languages, Status status, Pageable pageable) {
        return boardRepository.findBoardByTag(purpose, fields, languages, status, pageable);
    }

    private void saveFiledUrls(List<String> imageUrlList, Board board) {
        for (String imageUrl : imageUrlList) {
            boardFileRepository.save(
                    BoardImage.builder()
                            .board(board)
                            .imageUrl(imageUrl)
                            .build()
            );
        }
    }

    private void saveLanguages(List<Language> languageList, Board board) {
        for (Language language : languageList) {
            boardLanguageRepository.save(
                    BoardLanguage.builder()
                            .board(board)
                            .language(language)
                            .build()
            );
        }
    }

    private void saveFileds(List<Field> fieldList, Board board) {
        for (Field field : fieldList) {
            boardFieldRepository.save(
                    BoardField.builder()
                            .board(board)
                            .field(field)
                            .build()
            );
        }
    }

    private List<String> getSaveImageUrls(Board board, CorrectionPostRequestDto request){
        List<BoardImage> dbBoardImages = boardFileRepository.findByBoardId(board.getId());
        List<String> requestImages = request.getImageUrls();
        List<String> addImages = new ArrayList<>();

        if(CollectionUtils.isEmpty(dbBoardImages)) { // db에 url 이 없다면,
            if(!CollectionUtils.isEmpty(requestImages)) { // 요청파일 목록에 파일이 있다면,
                addImages.addAll(requestImages);// addFileList 에 fileUrls 을 추가
                return addImages;
            } else {
                return Collections.EMPTY_LIST;
            }
        } else { // db에 url 이 있다면,
            if(CollectionUtils.isEmpty(requestImages)){ // 요청파일 목록에 파일이 없다면,
                for (BoardImage dbBoardImage : dbBoardImages) {
                    boardFileRepository.deleteById(dbBoardImage.getId()); // BoardFile 테이블에서 해당 게시물의 url들을 삭제
                    awsS3Util.deleteS3(dbBoardImage.getImageUrl().substring(61));
                }
            } else { // 요청파일 목록에 파일이 있다면,
                List<String> dbBoardFileUrlList = new ArrayList<>();

                for (BoardImage dbBoardImage : dbBoardImages) {
                    if(!requestImages.contains(dbBoardImage.getImageUrl())){ // 요청파일 목록에 이미 db에 존재하는 파일이 존재하지 않는다면,
                        boardFileRepository.deleteById(dbBoardImage.getId()); // 해당 데이터 삭제
                        awsS3Util.deleteS3(dbBoardImage.getImageUrl().substring(61));
                    } else { // 요청 파일 목록에 이미 db에 존재하는 파일이 존재한다면,
                        dbBoardFileUrlList.add(dbBoardImage.getImageUrl());
                    }
                }

                for (String requestFileUrl : requestImages) {
                    if(!dbBoardFileUrlList.contains(requestFileUrl))
                        addImages.add(requestFileUrl);
                }

                return addImages;
            }
        }

        return Collections.EMPTY_LIST;
    }

}
