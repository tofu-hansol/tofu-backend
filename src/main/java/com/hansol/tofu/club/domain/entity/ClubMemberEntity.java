package com.hansol.tofu.club.domain.entity;

import com.hansol.tofu.club.enums.ClubJoinStatus;
import com.hansol.tofu.club.enums.ClubRole;
import com.hansol.tofu.global.TimeEntity;
import com.hansol.tofu.member.domain.MemberEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Getter
@EqualsAndHashCode(callSuper = false)
@Entity
@Builder
@DynamicInsert
@Table(name = "club_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ClubMemberEntity extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "club_role", nullable = false)
    @ColumnDefault("MEMBER")
    private ClubRole clubRole;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;

    @JoinColumn(name = "club_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ClubEntity club;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	@ColumnDefault("WAITING")
	private ClubJoinStatus clubJoinStatus;
}
