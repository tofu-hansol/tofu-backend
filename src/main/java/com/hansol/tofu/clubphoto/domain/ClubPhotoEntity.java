package com.hansol.tofu.clubphoto.domain;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import com.hansol.tofu.board.domain.entity.BoardEntity;
import com.hansol.tofu.global.TimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "club_photo")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ClubPhotoEntity extends TimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, name = "is_main_photo")
	@ColumnDefault("0")
	private Boolean isMainPhoto;

	@Column(nullable = false, name = "image_url")
	private String imageUrl;

	@JoinColumn(name = "board_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private BoardEntity board;

	public void changePhoto(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	// 연관관계 편의 메서드
	// public void setBoard(BoardEntity board) {
	// 	if(this.board != null) {
	// 		기존 보드가 존재한다면 관계를 끊는다.
	// this.board.getClubPhotos().remove(this);
	// }
	// this.board = board;
	// board.getClubPhotos().add(this);
	// }
}
