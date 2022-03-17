package com.server.Puzzle.service.attend;

import com.server.Puzzle.domain.attend.domain.Attend;
import com.server.Puzzle.domain.attend.domain.AttendStatus;
import com.server.Puzzle.domain.attend.dto.request.PatchAttendRequest;
import com.server.Puzzle.domain.attend.dto.response.GetAllAttendResponse;
import com.server.Puzzle.domain.attend.repository.AttendRepository;
import com.server.Puzzle.domain.attend.service.AttendService;
import com.server.Puzzle.domain.board.domain.Board;
import com.server.Puzzle.domain.board.dto.request.PostRequestDto;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest
public class AttendServiceTest {

    @Autowired
    private AttendService attendService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private AttendRepository attendRepository;

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
                .isFirstVisited(false)
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
    @DisplayName("프로젝트 참가 신청 테스트")
    void requestAttendTest(){
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

        boardService.post(postRequestDto);

        em.clear();
        em.close();

        // when
        Board board = boardRepository.findAll().get(0);
        attendService.requestAttend(board.getId());

        Attend attend = attendRepository.findAll().get(0);

        // then
        assertThat(attend).isNotNull();
    }

    @Test
    @DisplayName("프로젝트 참가 신청 전체 조회 테스트")
    void getAllAttend(){
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

        boardService.post(postRequestDto);

        em.clear();
        em.close();

        Board board = boardRepository.findAll().get(0);
        attendService.requestAttend(board.getId());

        em.clear();
        em.close();

        // when
        List<GetAllAttendResponse> allAttend = attendService.findAllAttend(board.getId());

        // then
        assertThat(allAttend).isNotNull();
    }

    @Test
    @DisplayName("프로젝트 참가 신청 수정 테스트")
    void patchAttend() {
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

        boardService.post(postRequestDto);

        em.clear();
        em.close();

        Board board = boardRepository.findAll().get(0);

        attendService.requestAttend(board.getId());
        Long attendId = attendRepository.findAll().get(0).getId();

        em.clear();
        em.close();

        PatchAttendRequest patchAttendRequest = PatchAttendRequest.builder()
                .attendId(attendId)
                .attendStatus(AttendStatus.ACCEPT)
                .build();

        // when
        attendService.patchAttend(board.getId(), patchAttendRequest);

        // then
        assertThat(attendRepository.findById(patchAttendRequest.getAttendId()).get().getAttendStatus()).isEqualTo(AttendStatus.ACCEPT);
    }
}
