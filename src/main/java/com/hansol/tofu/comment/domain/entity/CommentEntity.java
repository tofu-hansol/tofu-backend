package com.hansol.tofu.comment.domain.entity;

import com.hansol.tofu.board.domain.entity.BoardEntity;
import com.hansol.tofu.comment.enums.CommentStatus;
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
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CommentEntity extends TimeEntity {


    @Id
   	@GeneratedValue(strategy = GenerationType.IDENTITY)
   	private Long id;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @ColumnDefault("PUBLISHED")
    private CommentStatus commentStatus;

    @JoinColumn(name = "member_id")
    @OneToOne(fetch = FetchType.LAZY)
    private MemberEntity member;

    @JoinColumn(name = "board_id")
   	@ManyToOne(fetch = FetchType.LAZY)
   	private BoardEntity board;

    public void deleteComment() {
        this.commentStatus = CommentStatus.DELETED;
    }

    public void changeComment(String content) {
        this.content = content;
    }

}
