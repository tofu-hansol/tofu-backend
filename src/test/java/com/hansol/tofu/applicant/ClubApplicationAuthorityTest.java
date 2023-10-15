package com.hansol.tofu.applicant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hansol.tofu.mock.WithMockCustomUser;

@SpringBootTest
@AutoConfigureMockMvc
@Disabled
class ClubApplicationAuthorityTest {

	@Autowired
	private MockMvc mvc;

	private ClubApplicationService clubApplicationService;

	private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());


	@Test
	@WithMockCustomUser(username = "lisa@test.com")
	void applyClubSchedule_동호회에_가입되어있지않았을때_모임일정_참가신청에_실패한다() throws Exception {
		long notJoinedClubId = 4L;
		long scheduleId = 2L;
		mvc.perform(post("/api/clubs/" + notJoinedClubId + "/schedules/" + scheduleId + "/applicants")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().is5xxServerError())
			.andExpect(jsonPath("$.message", Matchers.is("Access Denied")));
	}


	@Test
	@WithMockCustomUser(username = "lisa@test.com")
	void cancelClubSchedule_동호회에_가입되어있지않았을때_모임일정_참가취소에_실패한다() throws Exception {
		long notJoinedClubId = 4L;
		long scheduleId = 2L;
		mvc.perform(delete("/api/clubs/" + notJoinedClubId + "/schedules/" + scheduleId + "/applicants")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().is5xxServerError())
			.andExpect(jsonPath("$.message", Matchers.is("Access Denied")));
	}

}
