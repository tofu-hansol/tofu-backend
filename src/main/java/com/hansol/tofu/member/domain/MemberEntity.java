package com.hansol.tofu.member.domain;

import java.util.Objects;

import com.hansol.tofu.global.TimeEntity;
import com.hansol.tofu.member.enums.MemberStatus;
import com.hansol.tofu.member.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode(callSuper = false)
@Entity
@Builder
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity extends TimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private Integer career;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private MemberStatus memberStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "user_role")
	private UserRole userRole;

	@PrePersist
	public void prePersist() {
		this.memberStatus = Objects.isNull(this.memberStatus) ? MemberStatus.ACTIVATE : this.memberStatus;
		this.userRole = Objects.isNull(this.userRole) ? UserRole.ROLE_USER : this.userRole;
	}

	private MemberEntity(Long id, String name, String email, String password, Integer career, MemberStatus memberStatus,
		UserRole userRole) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.career = career;
		this.memberStatus = memberStatus;
		this.userRole = userRole;
	}
}
