package com.hansol.tofu.clubphoto.domain;

import com.hansol.tofu.board.domain.entity.BoardEntity;
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
@Table(name = "club_photo")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ClubPhotoEntity extends TimeEntity {

    @Id
   	@GeneratedValue(strategy = GenerationType.IDENTITY)
   	private Long id;

    @Column(nullable = false, name = "is_main_photo")
    @ColumnDefault("0")
    private boolean isMainPhoto;

    @Column(nullable = false, name = "image_url")
    private String imageUrl;

    @JoinColumn(name = "board_id")
   	@ManyToOne(fetch = FetchType.LAZY)
   	private BoardEntity board;

    public void changePhoto(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
