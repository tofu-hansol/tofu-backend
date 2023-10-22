package com.hansol.tofu.clubmember.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hansol.tofu.clubmember.domain.entity.ClubMemberEntity;
import com.hansol.tofu.clubmember.store.ClubMemberQueryStore;

public interface ClubMemberRepository extends JpaRepository<ClubMemberEntity, Long>, ClubMemberQueryStore {

	Optional<ClubMemberEntity> findByClubIdAndMemberId(Long clubId, Long memberId);
}
