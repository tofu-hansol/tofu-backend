package com.hansol.tofu.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hansol.tofu.board.BoardService;
import com.hansol.tofu.board.domain.dto.BoardCreationRequestDTO;
import com.hansol.tofu.board.domain.dto.BoardEditRequestDTO;
import com.hansol.tofu.error.BaseExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BoardControllerTest {

    private MockMvc client;
    private BoardService boardService;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        boardService = mock(BoardService.class);
        client = MockMvcBuilders
                .standaloneSetup(new BoardController(boardService))
                .setControllerAdvice(new BaseExceptionHandler())
                .build();
    }


    @Test
    void deleteBoard_동호회_게시글삭제에_성공한다() throws Exception {
        Long clubId = 1L;
        Long boardId = 1L;


        client.perform(delete("/api/clubs/{clubId}/boards/{boardId}", clubId, boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        verify(boardService, times(1)).deleteBoard(clubId, boardId);
    }


}