package com.hansol.tofu.clubschedule.domain;

import com.hansol.tofu.club.domain.entity.ClubEntity;
import com.hansol.tofu.clubschedule.domain.dto.ClubScheduleEditRequestDTO;
import com.hansol.tofu.clubschedule.enums.ClubScheduleStatus;
import com.hansol.tofu.global.TimeEntity;
import jakarta.persistence.*;
import lombok.*;
import software.amazon.ion.Decimal;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@EqualsAndHashCode(callSuper = false)
@Entity
@Builder
@DynamicInsert
@Table(name = "club_schedule")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ClubScheduleEntity extends TimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, name = "event_at")
	private ZonedDateTime eventAt;

	@Column(nullable = false)
	private String title;

	@Column(name = "place_name", nullable = false)
	private String placeName;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	@ColumnDefault("RECRUITING")
	private ClubScheduleStatus clubScheduleStatus;

	@JoinColumn(name = "club_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private ClubEntity club;

	private Double latitude;

	private Double longitude;


	public void changeClubSchedule(ClubScheduleEditRequestDTO clubSchedule) {
		this.title = clubSchedule.title();
		this.placeName = clubSchedule.content();
		this.eventAt = ZonedDateTime.of(clubSchedule.eventAt(), ZoneId.of("Asia/Seoul"));
		this.latitude = clubSchedule.latitude();
		this.longitude = clubSchedule.longitude();
	}

	public void deleteClubSchedule() {
		this.clubScheduleStatus = ClubScheduleStatus.DELETED;
	}

}
