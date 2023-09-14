package com.hansol.tofu.club.repository;

import com.hansol.tofu.club.domain.entity.ClubMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubMemberRepository extends JpaRepository<ClubMemberEntity, Long> {

    List<ClubMemberEntity> findAllByMemberId(Long memberId);
}
