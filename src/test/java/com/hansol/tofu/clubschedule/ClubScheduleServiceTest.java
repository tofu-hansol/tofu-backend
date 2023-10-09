package com.hansol.tofu.clubschedule;

import com.hansol.tofu.clubschedule.domain.ClubScheduleCreationRequestDTO;
import com.hansol.tofu.clubschedule.repository.ClubScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

public class ClubScheduleServiceTest {

    private ClubScheduleService sut;
    private ClubScheduleRepository clubScheduleRepository;

    @BeforeEach
    void setUp() {
        clubScheduleRepository = mock(ClubScheduleRepository.class);
        sut = new ClubScheduleService(clubScheduleRepository);
    }

    @Test
    void addClubSchedule_동호회_모임일정을_추가한다() {
        var clubScheduleCreationRequestDTO = ClubScheduleCreationRequestDTO.builder()
                .eventAt(LocalDateTime.now().plusHours(1L))
                .title("두부먹는모임")
                .content("두부먹는모임입니다")
                .build();
        var clubScheduleEntity = clubScheduleCreationRequestDTO.toEntity(clubScheduleCreationRequestDTO);
        when(clubScheduleRepository.save(clubScheduleEntity)).thenReturn(clubScheduleEntity);


        sut.addClubSchedule(clubScheduleCreationRequestDTO);


        verify(clubScheduleRepository, times(1)).save(clubScheduleEntity);
    }




}
