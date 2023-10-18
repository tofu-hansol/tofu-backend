package com.hansol.tofu.clubmember.store;

import java.util.List;

import com.hansol.tofu.clubmember.domain.entity.ClubMemberEntity;

public interface ClubMemberQueryStore {

    List<ClubMemberEntity> findAllByMemberId(Long memberId);
}
