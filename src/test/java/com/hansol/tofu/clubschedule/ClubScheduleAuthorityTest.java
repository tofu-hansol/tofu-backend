package com.hansol.tofu.clubschedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hansol.tofu.clubschedule.domain.dto.ClubScheduleCreationRequestDTO;
import com.hansol.tofu.clubschedule.domain.dto.ClubScheduleEditRequestDTO;
import com.hansol.tofu.clubschedule.mock.WithMockCustomUser;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



//@WebMvcTest(ClubScheduleController.class)
//@MockBean(JpaMetamodelMappingContext.class)
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = {ClubScheduleController.class, ClubAuthorizationLogic.class})
//@WebAppConfiguration
@SpringBootTest
@AutoConfigureMockMvc
@Disabled
public class ClubScheduleAuthorityTest {

    @Autowired
    private MockMvc mvc;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    @WithMockCustomUser(username = "lisa@test.com")
    void addClubSchedule_일반유저_권한으로_모임일정_등록시_예외가_발생한다() throws Exception {
        var clubScheduleCreationRequestDTO = ClubScheduleCreationRequestDTO.builder()
                .eventAt(LocalDateTime.now().plusHours(1))
                .title("한솔두부")
                .content("한솔두부모임")
                .build();


        long clubId = 3L;
        mvc.perform(post("/api/clubs/" + clubId + "/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clubScheduleCreationRequestDTO)))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.message", Matchers.is("Access Denied")));
    }

    @Test
    @WithMockCustomUser(username = "lisa@test.com")
    void addClubSchedule_동호회장_권한으로_모임일정_등록시_등록에_성공한다() throws Exception {
        var clubScheduleCreationRequestDTO = ClubScheduleCreationRequestDTO.builder()
                .eventAt(LocalDateTime.now().plusHours(1))
                .title("한솔두부")
                .content("한솔두부모임")
                .build();


        mvc.perform(post("/api/clubs/{clubId}/schedules", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clubScheduleCreationRequestDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockCustomUser(username = "lisa@test.com")
    void addClubSchedule_총무_권한으로_모임일정_등록시_등록에_실패한다() throws Exception {
        var clubScheduleCreationRequestDTO = ClubScheduleCreationRequestDTO.builder()
                .eventAt(LocalDateTime.now().plusHours(1))
                .title("한솔두부")
                .content("한솔두부모임")
                .build();


        mvc.perform(post("/api/clubs/{clubId}/schedules", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clubScheduleCreationRequestDTO)))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.message", Matchers.is("Access Denied")));
    }

    @Test
    @WithMockCustomUser(username = "lisa@test.com")
    void editClubSchedule_총무_권한으로_모임일정_변경시_변경에_실패한다() throws Exception {
        var newEventAt = LocalDateTime.now().plusHours(1);
        var clubScheduleEditRequestDTO = ClubScheduleEditRequestDTO.builder()
                .eventAt(newEventAt)
                .title("변경된한솔두부모임")
                .content("변경된한솔두부모임입니다")
                .build();


        mvc.perform(patch("/api/clubs/{clubId}/schedules/2", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clubScheduleEditRequestDTO)))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.message", Matchers.is("Access Denied")));
    }

    @Test
    @WithMockCustomUser(username = "lisa@test.com")
    void editClubSchedule_동호회장_권한으로_모임일정_변경시_변경에_성공한다() throws Exception {
        var newEventAt = LocalDateTime.now().plusHours(1);
        var clubScheduleCreationRequestDTO = ClubScheduleCreationRequestDTO.builder()
                .eventAt(newEventAt)
                .title("변경된한솔두부모임")
                .content("변경된한솔두부모임입니다")
                .build();


        mvc.perform(patch("/api/clubs/{clubId}/schedules/2", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clubScheduleCreationRequestDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockCustomUser(username = "lisa@test.com")
    void editClubSchedule_동호회장_권한으로_모임일정_삭제에_성공한다() throws Exception {
        Long clubIdAsPresident = 1L;
        Long scheduleId = 2L;


        mvc.perform(delete("/api/clubs/" + clubIdAsPresident + "/schedules/" + scheduleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockCustomUser(username = "lisa@test.com")
    void editClubSchedule_총무_권한으로_모임일정_삭제에_성공한다() throws Exception {
        Long clubIdAsManager = 2L;
        Long scheduleId = 2L;


        mvc.perform(delete("/api/clubs/" + clubIdAsManager + "/schedules/" + scheduleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.message", Matchers.is("Access Denied")));
    }

}
