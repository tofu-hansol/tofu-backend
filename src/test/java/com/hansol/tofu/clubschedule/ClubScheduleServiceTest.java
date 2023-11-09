package com.hansol.tofu.clubschedule;

import com.hansol.tofu.club.domain.entity.ClubEntity;
import com.hansol.tofu.club.repository.ClubRepository;
import com.hansol.tofu.clubschedule.domain.dto.ClubScheduleCreationRequestDTO;
import com.hansol.tofu.clubschedule.domain.ClubScheduleEntity;
import com.hansol.tofu.clubschedule.domain.dto.ClubScheduleEditRequestDTO;
import com.hansol.tofu.clubschedule.enums.ClubScheduleStatus;
import com.hansol.tofu.clubschedule.repository.ClubScheduleRepository;
import com.hansol.tofu.error.BaseException;
import com.hansol.tofu.error.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class ClubScheduleServiceTest {

    private ClubScheduleService sut;
    private ClubScheduleRepository clubScheduleRepository;
    private ClubRepository clubRepository;

    @BeforeEach
    void setUp() {
        clubScheduleRepository = mock(ClubScheduleRepository.class);
        clubRepository = mock(ClubRepository.class);
        sut = new ClubScheduleService(clubScheduleRepository, clubRepository);
    }

    @Test
    void addClubSchedule_동호회_모임일정을_추가한다() {
        var clubEntity = ClubEntity.builder().id(2L).build();
        var clubScheduleCreationRequestDTO = ClubScheduleCreationRequestDTO.builder()
                .eventAt(LocalDateTime.now().plusHours(1L))
                .title("한솔두부모임")
                .content("한솔두부모임입니다")
				.latitude(37.123456)
				.longitude(127.123456)
                .build();
        var clubScheduleEntity = clubScheduleCreationRequestDTO.toEntity(clubEntity);
        when(clubRepository.findById(2L)).thenReturn(Optional.of(clubEntity));
        when(clubScheduleRepository.save(clubScheduleEntity)).thenReturn(clubScheduleEntity);


        sut.addClubSchedule(2L, clubScheduleCreationRequestDTO);


        verify(clubScheduleRepository, times(1)).save(clubScheduleEntity);
    }

    @Test
    void addClubSchedule_존재하지_않는_동호회에_모임일정_추가시_오류를_반환한다() {
        var clubScheduleCreationRequestDTO = ClubScheduleCreationRequestDTO.builder()
                .eventAt(LocalDateTime.now().plusHours(1L))
                .title("한솔두부모임")
                .content("한솔두부모임입니다")
				.latitude(37.123456)
				.longitude(127.123456)
                .build();
        when(clubRepository.findById(2L)).thenReturn(Optional.empty());


        var exception = assertThrows(BaseException.class, () -> sut.addClubSchedule(2L, clubScheduleCreationRequestDTO));


        assertEquals(ErrorCode.NOT_FOUND_CLUB.getMessage(), exception.getMessage());
    }

    @Test
    void editClubSchedule_동호회_모임일정을_변경한다() throws Exception {
        LocalDateTime newEventAt = LocalDateTime.now().plusHours(1);
        var clubScheduleEditRequestDTO = ClubScheduleEditRequestDTO.builder()
                .eventAt(newEventAt)
                .title("변경된한솔두부모임")
                .content("변경된한솔두부모임입니다")
				.latitude(37.123456)
				.longitude(127.123456)
                .build();
        var clubScheduleEntity = ClubScheduleEntity.builder()
                .eventAt(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Asia/Seoul")))
                .title("한솔두부모임")
                .content("한솔두부모임입니다")
				.latitude(11.111111)
				.longitude(22.222222)
                .build();
        when(clubScheduleRepository.findById(2L)).thenReturn(Optional.of(clubScheduleEntity));


        sut.editClubSchedule(2L, clubScheduleEditRequestDTO);


        assertEquals(ZonedDateTime.of(clubScheduleEditRequestDTO.eventAt(), ZoneId.of("Asia/Seoul")), clubScheduleEntity.getEventAt());
        assertEquals(clubScheduleEditRequestDTO.title(), clubScheduleEntity.getTitle());
        assertEquals(clubScheduleEditRequestDTO.content(), clubScheduleEntity.getContent());
		assertEquals(clubScheduleEditRequestDTO.latitude(), clubScheduleEntity.getLatitude());
		assertEquals(clubScheduleEditRequestDTO.longitude(), clubScheduleEntity.getLongitude());
    }

    @Test
    void deleteClubSchedule_동호회_모임일정을_삭제한다() throws Exception {
        var clubScheduleEntity = ClubScheduleEntity.builder()
                .id(2L)
                .eventAt(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Asia/Seoul")))
                .title("한솔두부모임")
                .content("한솔두부모임입니다")
                .clubScheduleStatus(ClubScheduleStatus.RECRUITING)
                .build();
        when(clubScheduleRepository.findById(2L)).thenReturn(Optional.of(clubScheduleEntity));


        sut.deleteClubSchedule(2L);


        assertEquals(clubScheduleEntity.getClubScheduleStatus(), ClubScheduleStatus.DELETED);
    }

}
