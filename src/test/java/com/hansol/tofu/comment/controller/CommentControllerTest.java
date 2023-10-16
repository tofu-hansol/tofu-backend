package com.hansol.tofu.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hansol.tofu.comment.CommentService;
import com.hansol.tofu.comment.domain.dto.CommentCreationRequestDTO;
import com.hansol.tofu.comment.domain.dto.CommentEditRequestDTO;
import com.hansol.tofu.error.BaseExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentControllerTest {

    private MockMvc client;
    private CommentService commentService;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        commentService = mock(CommentService.class);
        client = MockMvcBuilders
                .standaloneSetup(new CommentController(commentService))
                .setControllerAdvice(new BaseExceptionHandler())
                .build();
    }


    @Test
    void addComment_동호회_댓글작성에_성공한다() throws Exception {
        Long clubId = 1L;
        Long boardId = 2L;
        Long commentId = 3L;
        var commentCreationRequestDTO = CommentCreationRequestDTO.builder()
                .content("댓글내용")
                .build();

        client.perform(post("/api/clubs/{clubId}/boards/{boardId}/comments", clubId, boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentCreationRequestDTO)))
                .andExpect(status().isOk());


        verify(commentService, times(1)).addComment(boardId, commentCreationRequestDTO);
    }

    @Test
    void editComment_동호회_댓글수정에_성공한다() throws Exception {
        Long clubId = 1L;
        Long boardId = 2L;
        Long commentId = 3L;
        var commentEditRequestDTO = CommentEditRequestDTO.builder()
                .content("댓글내용수정")
                .build();


        client.perform(patch("/api/clubs/{clubId}/boards/{boardId}/comments/{commentId}", clubId, boardId, commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentEditRequestDTO)))
                .andExpect(status().isOk());


        verify(commentService, times(1)).editComment(boardId, commentId, commentEditRequestDTO);
    }

    @Test
    void deleteComment_동호회_댓글삭제에_성공한다() throws Exception {
        Long clubId = 1L;
        Long boardId = 2L;
        Long commentId = 3L;


        client.perform(delete("/api/clubs/{clubId}/boards/{boardId}/comments/{commentId}", clubId, boardId, commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        verify(commentService, times(1)).deleteComment(boardId, commentId);
    }

}