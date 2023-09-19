package com.hansol.tofu.club.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hansol.tofu.club.domain.entity.ClubMemberEntity;
import com.hansol.tofu.club.store.ClubMemberQueryStore;

public interface ClubMemberRepository extends JpaRepository<ClubMemberEntity, Long>, ClubMemberQueryStore {

}
