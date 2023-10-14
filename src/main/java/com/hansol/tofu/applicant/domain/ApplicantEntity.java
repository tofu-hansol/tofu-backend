package com.hansol.tofu.applicant.domain;

import org.hibernate.annotations.DynamicInsert;

import com.hansol.tofu.clubschedule.domain.ClubScheduleEntity;
import com.hansol.tofu.global.TimeEntity;
import com.hansol.tofu.member.domain.MemberEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode(callSuper = false)
@Entity
@Builder
@DynamicInsert
@Table(name = "applicant")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ApplicantEntity extends TimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JoinColumn(name = "club_schedule_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private ClubScheduleEntity clubSchedule;

	@JoinColumn(name = "member_id")
	@OneToOne(fetch = FetchType.LAZY)
	private MemberEntity member;

}
