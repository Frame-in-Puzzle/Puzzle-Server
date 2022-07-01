package com.server.Puzzle.service.attend;

import com.server.Puzzle.domain.attend.domain.Attend;
import com.server.Puzzle.domain.attend.dto.request.PatchAttendRequest;
import com.server.Puzzle.domain.attend.dto.response.FindAllAttendResponse;
import com.server.Puzzle.domain.attend.enumtype.AttendStatus;
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

    final PostRequestDto postRequestDto = PostRequestDto.builder()
            .title("title")
            .contents("contents")
            .purpose(Purpose.PROJECT)
            .status(Status.RECRUITMENT)
            .introduce("this is board")
            .fields(List.of(Field.BACKEND,Field.FRONTEND))
            .languages(List.of(Language.JAVA,Language.TS))
            .imageUrls(List.of("https://springbootpuzzletest.s3.ap-northeast-2.amazonaws.com/23752bbd-cd6e-4bde-986d-542df0517933.png"))
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
                .bio("성실한 개발자입니다")
                .url("https://github.com/KyungJunNoh")
                .profileImageUrl("https://avatars.githubusercontent.com/u/68670670?v=4")
                .isFirstVisited(false)
                .build();

        userRepository.save(user);

        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(user.getGithubId(),"password", List.of(Role.ROLE_USER));
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(token);

        User currentUser = currentUserUtil.getCurrentUser();

        // then
        assertNotNull(currentUser);
    }

    @Test
    void 프로젝트_참가_신청(){
        // given
        boardService.post(postRequestDto);

        em.clear();

        // when
        Board board = boardRepository.findAll().get(0);
        attendService.requestAttend(board.getId());

        Attend attend = attendRepository.findAll().get(0);

        // then
        assertThat(attend).isNotNull();
    }

    @Test
    void 프로젝트_참가_신청_전체_조회(){
        // given
        boardService.post(postRequestDto);

        em.clear();

        Board board = boardRepository.findAll().get(0);
        attendService.requestAttend(board.getId());

        em.clear();

        // when
        List<FindAllAttendResponse> allAttend = attendService.findAllAttend(board.getId());

        // then
        assertThat(allAttend).isNotNull();
    }

    @Test
    void 프로젝트_참가_신청_수정() {
        // given
        boardService.post(postRequestDto);

        em.clear();

        Board board = boardRepository.findAll().get(0);

        attendService.requestAttend(board.getId());
        Long attendId = attendRepository.findAll().get(0).getId();

        em.clear();

        PatchAttendRequest patchAttendRequest = PatchAttendRequest.builder()
                .attendStatus(AttendStatus.ACCEPT)
                .build();

        // when
        attendService.patchAttend(attendId, patchAttendRequest);

        // then
        assertThat(attendRepository.findById(attendId).get().getAttendStatus()).isEqualTo(AttendStatus.ACCEPT);
    }

    @Test
    void 프로젝트_참가_신청_취소(){
        // given
        boardService.post(postRequestDto);

        em.clear();

        Board board = boardRepository.findAll().get(0);
        Long boardId = board.getId();
        attendService.requestAttend(boardId);
        Long attendId = attendRepository.findAll().get(0).getId();
        em.clear();

        // when
        attendService.deleteAttend(boardId);

        // then
        assertThat(attendRepository.findById(attendId).isEmpty()).isTrue();
    }

}
