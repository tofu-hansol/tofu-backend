package com.hansol.tofu.member.store;

import java.util.Optional;

import com.hansol.tofu.member.domain.dto.MemberMyProfileResponseDTO;
import com.hansol.tofu.member.domain.dto.MemberProfileResponseDTO;

public interface MemberQueryStore {

	Optional<MemberMyProfileResponseDTO> findMyProfile(Long memberId);

	Optional<MemberProfileResponseDTO> findMemberProfile(Long memberId);
}
