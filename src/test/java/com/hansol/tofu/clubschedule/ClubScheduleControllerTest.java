package com.hansol.tofu.clubschedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hansol.tofu.clubschedule.controller.ClubScheduleController;
import com.hansol.tofu.clubschedule.domain.ClubScheduleCreationRequestDTO;
import com.hansol.tofu.error.BaseExceptionHandler;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ClubScheduleControllerTest {

    private MockMvc client;
    private ClubScheduleService clubScheduleService;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        clubScheduleService = mock(ClubScheduleService.class);
        client = MockMvcBuilders.standaloneSetup(new ClubScheduleController(clubScheduleService))
                .setControllerAdvice(new BaseExceptionHandler())
                .build();
    }

    @Test
    void addClubSchedule_모임일정추가요청시_성공한다() throws Exception {
        var clubScheduleCreationRequestDTO = ClubScheduleCreationRequestDTO.builder()
                .eventAt(LocalDateTime.now().plusHours(1L))
                .title("두부먹는모임")
                .content("두부먹는모임입니다")
                .build();

        client.perform(post("/api/club-schedule")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clubScheduleCreationRequestDTO)))
            .andExpect(status().isOk());
    }


    @Test
    void addClubSchedule_과거시간으로_모임추가시_예외가_발생한다() throws Exception {
        var clubScheduleCreationRequestDTO = ClubScheduleCreationRequestDTO.builder()
                .eventAt(LocalDateTime.now().minusHours(1))
                .title("두부먹는모임")
                .content("두부먹는모임입니다")
                .build();


        client.perform(post("/api/club-schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clubScheduleCreationRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is("현재 시간 이후의 일정만 등록할 수 있습니다")));
    }

}
