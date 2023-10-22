package com.hansol.tofu.board.domain.entity;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import com.hansol.tofu.board.enums.BoardStatus;
import com.hansol.tofu.club.domain.entity.ClubEntity;
import com.hansol.tofu.global.TimeEntity;
import com.hansol.tofu.member.domain.MemberEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BoardEntity extends TimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String content;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	@ColumnDefault("PUBLISHED")
	private BoardStatus boardStatus;

	@JoinColumn(name = "club_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private ClubEntity club;

	@JoinColumn(name = "member_id")
	@OneToOne(fetch = FetchType.LAZY)
	private MemberEntity member;

	// @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
	// private List<ClubPhotoEntity> clubPhotos = new ArrayList<>();

	public void changeBoard(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public void deleteBoard() {
		this.boardStatus = BoardStatus.DELETED;
	}

}
