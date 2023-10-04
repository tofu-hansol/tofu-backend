package com.hansol.tofu.member.domain;

import com.hansol.tofu.dept.domain.DeptEntity;
import com.hansol.tofu.global.TimeEntity;
import com.hansol.tofu.member.domain.dto.MemberEditRequestDTO;
import com.hansol.tofu.member.enums.MemberStatus;
import com.hansol.tofu.member.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import static com.hansol.tofu.member.enums.MemberStatus.*;

@Getter
@EqualsAndHashCode(callSuper = false)
@Entity
@Builder
@DynamicInsert
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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

    @Column(name = "profile_url")
    private String profileUrl;

    private String position;

    private String mbti;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @ColumnDefault("DORMANT")
    private MemberStatus memberStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @ColumnDefault("ROLE_USER")
    private UserRole userRole;

    @JoinColumn(name = "dept_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private DeptEntity dept;

    public void completeSignUp() {
        this.memberStatus = ACTIVATE;
    }

	public void changeMemberProfile(MemberEditRequestDTO req, DeptEntity dept) {
		this.password = req.password();
		this.name = req.name();
		this.position = req.position();
		this.mbti = req.mbti();
		this.dept = dept;
	}

	public void changeProfileImage(String profileUrl) {
		this.profileUrl = profileUrl;
	}

}
