package com.hansol.tofu.clubmember.store;

import java.util.List;

import com.hansol.tofu.clubmember.domain.dto.ClubJoinResponseDTO;
import com.hansol.tofu.clubmember.domain.entity.ClubMemberEntity;

public interface ClubMemberQueryStore {

    List<ClubMemberEntity> findAllByMemberId(Long memberId);

	List<ClubJoinResponseDTO> findClubJoinListBy(Long memberId);
}
