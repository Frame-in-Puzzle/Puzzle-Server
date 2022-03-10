package com.server.Puzzle.service.board;

import com.server.Puzzle.domain.board.domain.Board;
import com.server.Puzzle.domain.board.dto.request.CorrectionPostRequestDto;
import com.server.Puzzle.domain.board.dto.request.PostRequestDto;
import com.server.Puzzle.domain.board.dto.response.GetAllPostResponseDto;
import com.server.Puzzle.domain.board.dto.response.GetPostByTagResponseDto;
import com.server.Puzzle.domain.board.enumType.Purpose;
import com.server.Puzzle.domain.board.enumType.Status;
import com.server.Puzzle.domain.board.repository.BoardRepository;
import com.server.Puzzle.domain.board.service.BoardService;
import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.domain.user.repository.UserRepository;
import com.server.Puzzle.global.enumType.Field;
import com.server.Puzzle.global.enumType.Language;
import com.server.Puzzle.global.enumType.Role;
import com.server.Puzzle.global.util.CurrentUserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest
public class BoardServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private BoardService boardService;
    @Autowired
    private CurrentUserUtil currentUserUtil;
    @Autowired
    private EntityManager em;

    @BeforeEach
    @DisplayName("로그인 되어있는 유저를 확인하는 테스트")
    void currentUser() {
        // given // when
        User user = User.builder()
                .oauthIdx("1234")
                .email("developerjun0615@gmail.com")
                .githubId("KyungJunNoh2")
                .name("노경준")
                .field(Field.BACKEND)
                .roles(List.of(Role.USER))
                .bio("성실한 개발자입니다")
                .url("https://github.com/KyungJunNoh")
                .imageUrl("https://avatars.githubusercontent.com/u/68670670?v=4")
                .isFirstVisit(false)
                .build();

        userRepository.save(user);

        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(user.getGithubId(),"password",List.of(Role.USER));
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(token);

        User currentUser = currentUserUtil.getCurrentUser();

        // then
        assertNotNull(currentUser);
    }

    @Test
    @DisplayName("게시물 등록 테스트")
    void postTest(){
        // given
        PostRequestDto postRequestDto = PostRequestDto.builder()
                .title("title")
                .contents("contents")
                .purpose(Purpose.PROJECT)
                .status(Status.RECRUITMENT)
                .fieldList(List.of(Field.BACKEND,Field.FRONTEND))
                .languageList(List.of(Language.JAVA,Language.TS))
                .fileUrlList(List.of("google.com","naver.com"))
                .build();

        // when
        boardService.post(postRequestDto);
        em.flush();
        em.clear();
        Board board = boardRepository.findAll().get(0);

        // then
        assertThat(board.getTitle()).isEqualTo(postRequestDto.getTitle());

        assertThat(
                board.getBoardFields().stream()
                .map(b -> b.getField())
                .collect(Collectors.toList())
        ).isEqualTo(postRequestDto.getFieldList());

        assertThat(
                board.getBoardLanguages().stream()
                        .map(b -> b.getLanguage())
                        .collect(Collectors.toList())
        ).isEqualTo(postRequestDto.getLanguageList());

        assertThat(
                board.getBoardFiles().stream()
                .map(b -> b.getUrl())
                .collect(Collectors.toList())
        ).isEqualTo(postRequestDto.getFileUrlList());
    }

    @Test
    @DisplayName("게시물 수정 테스트")
    void correctionPostTest(){
        // given
        PostRequestDto postRequestDto = PostRequestDto.builder()
                .title("title")
                .contents("contents")
                .purpose(Purpose.PROJECT)
                .status(Status.RECRUITMENT)
                .fieldList(List.of(Field.BACKEND,Field.FRONTEND))
                .languageList(List.of(Language.JAVA,Language.TS))
                .fileUrlList(List.of("https://springbootpuzzletest.s3.ap-northeast-2.amazonaws.com/23752bbd-cd6e-4bde-986d-542df0517933.png"))
                .build();

        CorrectionPostRequestDto correctionPostRequestDto = CorrectionPostRequestDto.builder()
                .title("correctionTitle")
                .contents("correctionContents")
                .purpose(Purpose.PROJECT)
                .status(Status.RECRUITMENT)
                .fileUrlList(List.of("https://springbootpuzzletest.s3.ap-northeast-2.amazonaws.com/23752bbd-cd6e-4bde-986d-542df0517933.png"))
                .languageList(List.of(Language.PYTORCH, Language.KOTLIN))
                .fieldList(List.of(Field.AI,Field.ANDROID))
                .build();

        boardService.post(postRequestDto);

        Long boardId = boardRepository.findAll().get(0).getId();

        // when
        boardService.correctionPost(boardId, correctionPostRequestDto);

        Board correctionBoard = boardRepository.findAll().get(0);

        // then
        assertThat(correctionBoard.getTitle()).isEqualTo("correctionTitle");
    }

    @Test
    @DisplayName("게시물 전체 조회 테스트")
    void getAllPostTest(){
        // given
        PostRequestDto postRequestDto = PostRequestDto.builder()
                .title("title")
                .contents("contents")
                .purpose(Purpose.PROJECT)
                .status(Status.RECRUITMENT)
                .fieldList(List.of(Field.BACKEND,Field.FRONTEND))
                .languageList(List.of(Language.JAVA,Language.TS))
                .fileUrlList(List.of("https://springbootpuzzletest.s3.ap-northeast-2.amazonaws.com/23752bbd-cd6e-4bde-986d-542df0517933.png"))
                .build();

        for (int i = 0; i < 12; i++){
            boardService.post(postRequestDto);
        }

        // when
        Page<GetAllPostResponseDto> allPost = boardService.getAllPost(PageRequest.of(1, 12));

        // then
        assertThat(allPost.getSize()).isEqualTo(12);
    }

    @Test
    @DisplayName("게시물 단일 조회 테스트")
    void getPostTest(){
        // given
        PostRequestDto postRequestDto = PostRequestDto.builder()
                .title("title")
                .contents("contents")
                .purpose(Purpose.PROJECT)
                .status(Status.RECRUITMENT)
                .fieldList(List.of(Field.BACKEND,Field.FRONTEND))
                .languageList(List.of(Language.JAVA,Language.TS))
                .fileUrlList(List.of("https://springbootpuzzletest.s3.ap-northeast-2.amazonaws.com/23752bbd-cd6e-4bde-986d-542df0517933.png"))
                .build();

        // when
        boardService.post(postRequestDto);
        Board findBoard = boardRepository.findAll().get(0);

        em.flush();
        em.clear();

        // then
        assertThat(boardRepository.findById(findBoard.getId()).get().getTitle()).isEqualTo(postRequestDto.getTitle());
    }

    @Disabled
    @Test
    @DisplayName("게시물 삭제 테스트")
    void deletePost(){
        // then
        PostRequestDto postRequestDto = PostRequestDto.builder()
                .title("title")
                .contents("contents")
                .purpose(Purpose.PROJECT)
                .status(Status.RECRUITMENT)
                .fieldList(List.of(Field.BACKEND,Field.FRONTEND))
                .languageList(List.of(Language.JAVA,Language.TS))
                .fileUrlList(List.of("https://springbootpuzzletest.s3.ap-northeast-2.amazonaws.com/23752bbd-cd6e-4bde-986d-542df0517933.png"))
                .build();

        boardService.post(postRequestDto);

        // when
        Long boardId = boardRepository.findAll().get(0).getId();
        boardService.deletePost(boardId);

        // then
        assertThat(boardRepository.findById(boardId)).isNull();
    }

    @Test
    @DisplayName("태그로 게시물을 조회하는 테스트")
    void getPostByTagTest() {
        // given
        PostRequestDto postRequestDto1 = PostRequestDto.builder()
                .title("title1")
                .contents("contents1")
                .purpose(Purpose.PROJECT)
                .status(Status.RECRUITMENT)
                .fieldList(List.of(Field.BACKEND,Field.FRONTEND))
                .languageList(List.of(Language.JAVA,Language.TS))
                .fileUrlList(List.of("https://springbootpuzzletest.s3.ap-northeast-2.amazonaws.com/23752bbd-cd6e-4bde-986d-542df0517933.png"))
                .build();

        PostRequestDto postRequestDto2 = PostRequestDto.builder()
                .title("title2")
                .contents("contents2")
                .purpose(Purpose.SERVICE)
                .status(Status.RECRUITMENT)
                .fieldList(List.of(Field.BACKEND,Field.FRONTEND))
                .languageList(List.of(Language.SPRINGBOOT,Language.REACT))
                .fileUrlList(List.of("https://springbootpuzzletest.s3.ap-northeast-2.amazonaws.com/23752bbd-cd6e-4bde-986d-542df0517933.png"))
                .build();

        PostRequestDto postRequestDto3 = PostRequestDto.builder()
                .title("title3")
                .contents("contents3")
                .purpose(Purpose.STUDY)
                .status(Status.RECRUITMENT)
                .fieldList(List.of(Field.AI,Field.GAME))
                .languageList(List.of(Language.PYTORCH,Language.UNITY))
                .fileUrlList(List.of("https://springbootpuzzletest.s3.ap-northeast-2.amazonaws.com/23752bbd-cd6e-4bde-986d-542df0517933.png"))
                .build();

        boardService.post(postRequestDto1);
        boardService.post(postRequestDto2);
        boardService.post(postRequestDto3);

        em.clear();
        em.flush();

        // when
        Page<GetPostByTagResponseDto> post = boardService.getPostByTag(
                Purpose.PROJECT,
                List.of(Field.BACKEND),
                List.of(Language.JAVA),
                Status.RECRUITMENT,
                PageRequest.of(0, 12)
        );


        // then
        assertThat(post.getContent().get(0).getTitle()).isEqualTo("title1");
    }
}
