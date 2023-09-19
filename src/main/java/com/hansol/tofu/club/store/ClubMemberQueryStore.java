package com.hansol.tofu.club.store;

import java.util.List;

import com.hansol.tofu.club.domain.entity.ClubMemberEntity;

public interface ClubMemberQueryStore {

    List<ClubMemberEntity> findAllByMemberId(Long memberId);
}
