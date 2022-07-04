package com.server.Puzzle.exception.board;

import com.server.Puzzle.domain.board.domain.Board;
import com.server.Puzzle.domain.board.dto.request.CorrectionPostRequestDto;
import com.server.Puzzle.domain.board.dto.request.PostRequestDto;
import com.server.Puzzle.domain.board.enumType.Purpose;
import com.server.Puzzle.domain.board.enumType.Status;
import com.server.Puzzle.domain.board.repository.BoardRepository;
import com.server.Puzzle.domain.board.service.BoardService;
import com.server.Puzzle.domain.user.domain.Roles;
import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.domain.user.domain.UserLanguage;
import com.server.Puzzle.domain.user.repository.RolesRepository;
import com.server.Puzzle.domain.user.repository.UserLanguageRepository;
import com.server.Puzzle.domain.user.repository.UserRepository;
import com.server.Puzzle.global.enumType.Field;
import com.server.Puzzle.global.enumType.Language;
import com.server.Puzzle.global.enumType.Role;
import com.server.Puzzle.global.exception.CustomException;
import com.server.Puzzle.global.util.CurrentUserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

import static com.server.Puzzle.global.enumType.Language.SPRINGBOOT;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
public class BoardExceptionTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    @Autowired
    private EntityManager em;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private UserLanguageRepository userLanguageRepository;

    final PostRequestDto postRequestDto = PostRequestDto.builder()
            .title("title")
            .contents("contents")
            .purpose(Purpose.PROJECT)
            .status(Status.RECRUITMENT)
            .introduce("this is board")
            .fieldList(List.of(Field.BACKEND,Field.FRONTEND))
            .languageList(List.of(Language.JAVA,Language.TS))
            .fileUrlList(List.of("https://springbootpuzzletest.s3.ap-northeast-2.amazonaws.com/23752bbd-cd6e-4bde-986d-542df0517933.png"))
            .build();

    @BeforeEach
    void 로그인_되어있는_유저를_확인() {
        // given // when
        User user = User.builder()
                .oauthIdx("1234")
                .email("developerjun0615@gmail.com")
                .githubId("KyungJunNoh2")
                .name("노경준")
                .field(Field.BACKEND)
                .roles(Collections.singletonList(Roles.builder()
                        .role(Role.ROLE_USER).build()))
                .bio("성실한 개발자입니다")
                .url("https://github.com/KyungJunNoh")
                .profileImageUrl("https://avatars.githubusercontent.com/u/68670670?v=4")
                .isFirstVisited(false)
                .build();

        userRepository.save(user);

        UserLanguage userLanguage = UserLanguage.builder()
                .id(null)
                .language(SPRINGBOOT)
                .user(user)
                .build();

        Roles roles = Roles.builder()
                .id(null)
                .role(Role.ROLE_USER)
                .user(user)
                .build();

        userLanguageRepository.save(userLanguage);
        rolesRepository.save(roles);

        em.flush();
        em.clear();

        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(user.getGithubId(),"password",List.of(Role.ROLE_USER));
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(token);

        User currentUser = currentUserUtil.getCurrentUser();

        // then
        assertNotNull(currentUser);
    }

    @Test
    void correctionPost_에서_게시글을_찾을_수_없습니다(){
        // given
        CorrectionPostRequestDto correctionPostRequestDto = CorrectionPostRequestDto.builder()
                .title("correctionTitle")
                .contents("correctionContents")
                .purpose(Purpose.PROJECT)
                .status(Status.RECRUITMENT)
                .introduce("this is board")
                .fileUrlList(List.of("https://springbootpuzzletest.s3.ap-northeast-2.amazonaws.com/23752bbd-cd6e-4bde-986d-542df0517933.png"))
                .languageList(List.of(Language.PYTORCH, Language.KOTLIN))
                .fieldList(List.of(Field.AI,Field.ANDROID))
                .build();

        boardService.post(postRequestDto);

        Long boardId = boardRepository.findAll().get(0).getId() + 1L;

        // when // then
        CustomException customException = assertThrows(CustomException.class, () -> {
            boardService.correctionPost(boardId, correctionPostRequestDto);
        });

        assertEquals("게시글을 찾을 수 없습니다.", customException.getErrorCode().getDetail());
    }

    @Test
    void getPost_에서_게시글을_찾을_수_없습니다(){
        // given
        boardService.post(postRequestDto);

        Long boardId = boardRepository.findAll().get(0).getId() + 1L;

        // when // then
        CustomException customException = assertThrows(CustomException.class, () -> {
            boardService.getPost(boardId);
        });

        assertEquals("게시글을 찾을 수 없습니다.", customException.getErrorCode().getDetail());
    }

    @Test
    void deletePost_에서_게시글을_찾을_수_없습니다() {
        // given
        boardService.post(postRequestDto);

        Long boardId = boardRepository.findAll().get(0).getId() + 1L;

        // when // then
        CustomException customException = assertThrows(CustomException.class, () -> {
            boardService.deletePost(boardId);
        });

        assertEquals("게시글을 찾을 수 없습니다.", customException.getErrorCode().getDetail());
    }

    @Test
    void deletePost_에서_처리_권한이_없습니다(){
        // given
        User user = User.builder()
                .oauthIdx("1234")
                .email("hello@gmail.com")
                .githubId("JUN")
                .name("노경준")
                .field(Field.BACKEND)
                .bio("개발자입니다.")
                .profileImageUrl("naver.com")
                .refreshToken("aldkfja;ljflas;jdfsa")
                .isFirstVisited(false)
                .build();

        userRepository.save(user);

        Board board = Board.builder()
                .title("title")
                .contents("contents")
                .purpose(Purpose.PROJECT)
                .status(Status.RECRUITMENT)
                .introduce("this is board")
                .user(user)
                .build();

        boardRepository.save(board);

        // when
        CustomException customException = assertThrows(CustomException.class, () -> {
            boardService.deletePost(board.getId());
        });

        assertEquals("해당 요청을 처리할 권한이 존재하지 않습니다.", customException.getErrorCode().getDetail());
    }
}
