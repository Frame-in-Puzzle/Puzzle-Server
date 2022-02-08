package com.server.Puzzle.service.board;

import com.server.Puzzle.domain.board.domain.Board;
import com.server.Puzzle.domain.board.dto.request.CorrectionPostRequestDto;
import com.server.Puzzle.domain.board.dto.request.PostRequestDto;
import com.server.Puzzle.domain.board.enumType.Purpose;
import com.server.Puzzle.domain.board.enumType.Status;
import com.server.Puzzle.domain.board.service.BoardService;
import com.server.Puzzle.domain.user.domain.User;
import com.server.Puzzle.domain.user.repository.UserRepository;
import com.server.Puzzle.global.enumType.Field;
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

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
public class BoardServiceTest {

    @Autowired private BoardService boardService;
    @Autowired private UserRepository userRepository;
    @Autowired private CurrentUserUtil currentUserUtil;

    @BeforeEach
    @DisplayName("로그인 되어있는 유저를 확인하는 테스트")
    void currentUser() {
        // given // when
        User user = User.builder()
                .oauthId("1234")
                .email("developerjun0615@gmail.com")
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
                = new UsernamePasswordAuthenticationToken(user.getName(),"password",List.of(Role.USER));
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(token);

        User currentUser = currentUserUtil.getCurrentUser();

        // then
        assertEquals(user.getEmail(),currentUser.getEmail());
    }

//    @Test
//    @DisplayName("게시물을 등록 기능을 테스트하는 테스트")
//    void postTest(){
//        PostRequestDto requestDto = PostRequestDto.builder()
//                .title("string")
//                .contents("contents")
//                .purpose(Purpose.PROJECT)
//                .status(Status.RECRUITMENT)
//                .build();
//
//        Board post = boardService.post(null, requestDto);
//
//        assertThat(post.getTitle()).isEqualTo(requestDto.getTitle());
//    }

//    @Test
//    @DisplayName("게시물을 수정 기능을 테스트하는 테스트")
//    void correctionPostTest(){
//        PostRequestDto postRequestDto = PostRequestDto.builder()
//                .title("string")
//                .contents("contents")
//                .purpose(Purpose.PROJECT)
//                .status(Status.RECRUITMENT)
//                .build();
//
//        Board post = boardService.post(null, postRequestDto);
//        Long postId = post.getId();
//
//        CorrectionPostRequestDto correctionPostRequestDto = CorrectionPostRequestDto.builder()
//                .title("string2")
//                .contents("contents2")
//                .purpose(Purpose.PROJECT)
//                .status(Status.RECRUITMENT)
//                .build();
//
//        Board correctionPost = boardService.correctionPost(postId,correctionPostRequestDto);
//
//        assertEquals(correctionPost.getTitle(),"string2");
//    }
}

