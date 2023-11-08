package com.hansol.tofu.club.domain.entity;

import com.hansol.tofu.category.domain.CategoryEntity;
import com.hansol.tofu.club.domain.dto.ClubEditRequestDTO;
import com.hansol.tofu.club.enums.ClubStatus;
import com.hansol.tofu.global.TimeEntity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Getter
@EqualsAndHashCode(callSuper = false)
@Entity
@Builder
@DynamicInsert
@Table(name = "club")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ClubEntity extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "profile_url")
    private String profileUrl;

    @Column(name = "background_url")
    private String backgroundUrl;

    private String description;

    @Column(name = "account_number")
    private String accountNumber;

	@ColumnDefault("0")
    private Integer fee;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @ColumnDefault("ACTIVATE")
    private ClubStatus clubStatus;

    @JoinColumn(name = "category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CategoryEntity category;

	public void changeClubInfo(ClubEditRequestDTO club, CategoryEntity category) {
		this.description = club.description();
		this.accountNumber = club.accountNumber();
		this.fee = club.fee();
		this.category = category;
	}

	public void changeBackgroundImage(String backgroundUrl) {
		this.backgroundUrl = backgroundUrl;
	}

	public void changeProfileImage(String profileUrl) {
		this.profileUrl = profileUrl;
	}
}
