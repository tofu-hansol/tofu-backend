package com.hansol.tofu.clubschedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hansol.tofu.clubschedule.controller.ClubScheduleController;
import com.hansol.tofu.clubschedule.domain.dto.ClubScheduleCreationRequestDTO;
import com.hansol.tofu.clubschedule.domain.dto.ClubScheduleEditRequestDTO;
import com.hansol.tofu.error.BaseExceptionHandler;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
        client = MockMvcBuilders
                .standaloneSetup(new ClubScheduleController(clubScheduleService))
                .setControllerAdvice(new BaseExceptionHandler())
                .build();
    }

    @Test
    void addClubSchedule_모임일정추가요청시_성공한다() throws Exception {
        var clubScheduleCreationRequestDTO = ClubScheduleCreationRequestDTO.builder()
                .eventAt(LocalDateTime.now().plusHours(1L))
                .title("한솔두부")
                .content("한솔두부모임")
                .build();

        client.perform(post("/api/clubs/{clubId}/schedules", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clubScheduleCreationRequestDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }


    @Test
    void addClubSchedule_과거시간으로_모임추가시_예외가_발생한다() throws Exception {
        var clubScheduleCreationRequestDTO = ClubScheduleCreationRequestDTO.builder()
                .eventAt(LocalDateTime.now().minusHours(1))
                .title("한솔두부")
                .content("한솔두부모임")
                .build();


        client.perform(post("/api/clubs/{clubId}/schedules", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clubScheduleCreationRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is("현재 시간 이후의 일정만 등록할 수 있습니다")));
    }

    @Test
    void editClubSchedule_모임일정변경에_성공한다() throws Exception {
        var newEventAt = LocalDateTime.now().plusHours(1);
        var clubScheduleEditRequestDTO = ClubScheduleEditRequestDTO.builder()
                .eventAt(newEventAt)
                .title("변경된한솔두부모임")
                .content("변경된한솔두부모임입니다")
                .build();

        client.perform(patch("/api/clubs/{clubId}/schedules/2", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clubScheduleEditRequestDTO)))
                .andExpect(status().isOk());
    }

}
