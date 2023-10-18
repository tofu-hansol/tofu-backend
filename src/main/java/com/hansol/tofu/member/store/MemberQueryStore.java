package com.hansol.tofu.member.store;

import java.util.List;

import com.hansol.tofu.member.domain.dto.MemberMyProfileResponseDTO;

public interface MemberQueryStore {

    List<MemberMyProfileResponseDTO> findMyProfile(Long memberId);
}
