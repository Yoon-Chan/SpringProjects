package com.chan.sns.controller;

import com.chan.sns.controller.request.PostCommentRequest;
import com.chan.sns.controller.request.PostCreateRequest;
import com.chan.sns.controller.request.PostModifyRequest;
import com.chan.sns.exception.ErrorCode;
import com.chan.sns.exception.SnsApplicationException;
import com.chan.sns.fixture.PostEntityFixture;
import com.chan.sns.model.Post;
import com.chan.sns.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PostService postService;

    @Test
    @WithMockUser
    void 포스트작성() throws Exception {
        String title = "title";
        String body = "body";

        mockMvc.perform(
                        post("/api/v1/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body)))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 포스트작성시_로그인하지않는경우() throws Exception {
        String title = "title";
        String body = "body";

        //로그인하지 않는 경우

        mockMvc.perform(
                        post("/api/v1/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void 포스트수정() throws Exception {
        String title = "title";
        String body = "body";

        when(postService.modify(eq(title), eq(body), any(), any()))
                .thenReturn(Post.fromEntity(PostEntityFixture.get("userName", 1, 1)));

        mockMvc.perform(
                        put("/api/v1/posts/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body)))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void 포스트수정시_본인이_작성한_글이_아니라면_에러발생() throws Exception {
        String title = "title";
        String body = "body";

        doThrow(new SnsApplicationException(ErrorCode.INVALID_PERMISSION)).when(postService).modify(eq(title), eq(body), any(), eq(1));

        mockMvc.perform(
                        put("/api/v1/posts/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void 포스트수정시_수정하려는_글이_없는경우_에러발생() throws Exception {
        String title = "title";
        String body = "body";

        doThrow(new SnsApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).modify(eq(title), eq(body), any(), eq(1));

        mockMvc.perform(
                        put("/api/v1/posts/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body)))
                ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithAnonymousUser
    void 포스트수정시_로그인하지않는경우() throws Exception {
        String title = "title";
        String body = "body";

        //로그인하지 않는 경우

        mockMvc.perform(
                        put("/api/v1/posts/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void 포스트삭제() throws Exception {
        mockMvc.perform(
                        delete("/api/v1/posts/1")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void 포스트삭제시_작성자와_삭제요청자가_다를경우() throws Exception {
        doThrow(new SnsApplicationException(ErrorCode.INVALID_PERMISSION)).when(postService).delete(any(), any());

        mockMvc.perform(
                        delete("/api/v1/posts/1")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void 포스트삭제시_포스트가_존재하지_않는경우() throws Exception {
        doThrow(new SnsApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).delete(any(), any());

        mockMvc.perform(
                        delete("/api/v1/posts/1")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithAnonymousUser
    void 포스트삭제_로그인하지_않은경우() throws Exception {
        mockMvc.perform(
                        delete("/api/v1/posts/1")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void 피드목록() throws Exception {
        //mocking
        when(postService.list(any())).thenReturn(Page.empty());

        mockMvc.perform(
                        get("/api/v1/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 피드목록요청시_로그인하지_않은경우() throws Exception {
        mockMvc.perform(
                        get("/api/v1/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void 내피드목록() throws Exception {
        //mocking
        when(postService.my(any(), any())).thenReturn(Page.empty());

        mockMvc.perform(
                        get("/api/v1/posts/my")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 내피드목록요청시_로그인하지_않은경우() throws Exception {
        mockMvc.perform(
                        get("/api/v1/posts/my")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void 좋아요기능() throws Exception {
        mockMvc.perform(
                        post("/api/v1/posts/1/likes")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 좋아요_버튼_클릭시_로그인하지_않는경우() throws Exception {
        mockMvc.perform(
                        post("/api/v1/posts/1/likes")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void 좋아요_버튼_클릭시_게시글이_없는경우() throws Exception {
        doThrow(new SnsApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).like(any(), any());

        mockMvc.perform(
                        post("/api/v1/posts/1/likes")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void 댓글기능() throws Exception {
        mockMvc.perform(
                        post("/api/v1/posts/1/comments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new PostCommentRequest(("comment"))))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 댓글_작성시_로그인하지_않는경우() throws Exception {
        mockMvc.perform(
                        post("/api/v1/posts/1/comments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new PostCommentRequest(("comment"))))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void 댓글_작성시_게시글이_없는경우() throws Exception {
        doThrow(new SnsApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).comment(any(), any(), any());

        mockMvc.perform(
                        post("/api/v1/posts/1/comments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new PostCommentRequest(("comment"))))
                ).andDo(print())
                .andExpect(status().isNotFound());
    }
}
