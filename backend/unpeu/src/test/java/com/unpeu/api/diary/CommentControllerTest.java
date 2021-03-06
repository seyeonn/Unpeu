package com.unpeu.api.diary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unpeu.config.auth.JwtTokenUtil;
import com.unpeu.domain.entity.Board;
import com.unpeu.domain.entity.Comment;
import com.unpeu.domain.entity.User;
import com.unpeu.domain.repository.IBoardRepository;
import com.unpeu.domain.repository.ICommentRepository;
import com.unpeu.domain.repository.IUserRepository;
import com.unpeu.domain.request.CommentPostReq;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IBoardRepository boardRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ICommentRepository commentRepository;

    @Autowired
    private WebApplicationContext context;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // ?????? ?????? ??????
                .apply(springSecurity()) // Spring Security??? Spring MVC ???????????? ????????? ??? ????????? ?????? ?????? ????????? ??????
                .alwaysDo(print())
                .build();
    }

    public User saveUser() {
        return userRepository.save(User.builder()
                .id(1L)
                .userLogin("test@naver.com")
                .socialDomain("kakao").userName("?????????")
                .build());
    }

    public Board saveBoard() {
        User user = saveUser();
        Board save = boardRepository.save(Board.builder()
                .category("220505_????????????")
                .title("????????????? ????????????!")
                .content("????????? ??? ????????? ????????????")
                .user(user)
                .build());

        return save;
    }

    public Comment saveComment() {
        Board board = saveBoard();
        Comment comment = Comment.builder()
                .writer("?????????")
                .password("1234")
                .content("???????????????")
                .build();
        comment.setBoard(board);

        return commentRepository.save(comment);
    }

    /**
     * ?????? ??????
     */
    @Test
    public void ??????_??????_??????() throws Exception {
        // given
        Long boardId = saveBoard().getBoardId();
        CommentPostReq createComment = new CommentPostReq("?????????", "1234", "??????????????????");
        String content = objectMapper.writeValueAsString(createComment);

        // when
        mockMvc.perform(post("/api/comment/" + boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk());

        // then
        Optional<Board> oBoard = boardRepository.findById(boardId);
        Assertions.assertThat(oBoard.get().getComments().size()).isEqualTo(1);
    }

    /**
     * ????????? ????????? ???????????? ?????? ??????
     */
    @Test
    public void ??????_??????_???????????????_??????() throws Exception {
        // given
        Long boardId = 0L; // ????????? ??? ?????? ??????
        CommentPostReq createComment = new CommentPostReq("?????????", "1234", "??????????????????");
        String content = objectMapper.writeValueAsString(createComment);

        // when
        mockMvc.perform(post("/api/comment/" + boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isNotFound());
    }

    /**
     * ?????? ??????
     */
    @Test
    public void ??????_??????_??????() throws Exception {
        // given
        Long commentId = saveComment().getCommentId();
        CommentPostReq updateComment = new CommentPostReq("?????????", "1234", "??????????????????");
        String content = objectMapper.writeValueAsString(updateComment);

        // when
        mockMvc.perform(put("/api/comment/"+commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk());

        // then
        Optional<Comment> oComment = commentRepository.findById(commentId);
        Assertions.assertThat(oComment.get().getContent()).isEqualTo(updateComment.getContent());
    }

    /**
     * ????????? ?????? ??????????????? ?????? ?????? ??????
     * ?????????????????? ??????
     */
    @Test
    public void ??????_??????_????????????_??????() throws Exception {
        // given
        Long commentId = saveComment().getCommentId();
        CommentPostReq updateComment = new CommentPostReq("?????????", "5678", "??????????????????");
        String content = objectMapper.writeValueAsString(updateComment);

        // when
        mockMvc.perform(put("/api/comment/"+commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isForbidden());

        // then
        Optional<Comment> oComment = commentRepository.findById(commentId);
        Assertions.assertThat(oComment.get().getContent()).isEqualTo(saveComment().getContent());
    }

    /**
     * ?????? ??????
     */
    @Test
    public void ??????_??????_??????() throws Exception {
        // given
        Long commentId = saveComment().getCommentId();
        String password = "1234";

        // when
        mockMvc.perform(delete("/api/comment/" + commentId + "/" + password))
                .andExpect(status().isOk());

        // then
        Assertions.assertThat(commentRepository.findById(commentId)).isEmpty();
    }

    /**
     * ????????? ?????? ??????????????? ?????? ?????? ??????
     * ?????????????????? ??????
     */
    @Test
    public void ??????_??????_????????????_??????() throws Exception {
        // given
        Long commentId = saveComment().getCommentId();
        String password = "5678";

        // when
        mockMvc.perform(delete("/api/comment/" + commentId + "/" + password))
                .andExpect(status().isForbidden());

        // then
        Assertions.assertThat(commentRepository.findById(commentId)).isPresent();
    }
}