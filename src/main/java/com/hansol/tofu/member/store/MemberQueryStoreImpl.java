package com.hansol.tofu.member.store;

import static com.hansol.tofu.company.domain.QCompanyEntity.*;
import static com.hansol.tofu.dept.domain.QDeptEntity.*;
import static com.hansol.tofu.member.domain.QMemberEntity.*;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.hansol.tofu.member.domain.dto.MemberMyProfileResponseDTO;
import com.hansol.tofu.member.domain.dto.MemberProfileResponseDTO;
import com.hansol.tofu.member.domain.dto.QMemberMyProfileResponseDTO;
import com.hansol.tofu.member.domain.dto.QMemberProfileResponseDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberQueryStoreImpl implements MemberQueryStore {

	private final JPAQueryFactory queryFactory;

	@Override
	public Optional<MemberMyProfileResponseDTO> findMyProfile(Long memberId) {
		return Optional.ofNullable(queryFactory
			.select(new QMemberMyProfileResponseDTO(
				memberEntity.email,
				memberEntity.name,
				memberEntity.profileUrl,
				companyEntity.name,
				deptEntity.name,
				memberEntity.position,
				memberEntity.mbti
			))
			.from(memberEntity)
			.leftJoin(deptEntity).on(memberEntity.dept.id.eq(deptEntity.id)).fetchJoin()
			.leftJoin(companyEntity).on(deptEntity.company.id.eq(companyEntity.id)).fetchJoin()
			.distinct()
			.where(memberEntity.id.eq(memberId))
			.fetchOne());
	}

	@Override
	public Optional<MemberProfileResponseDTO> findMemberProfile(Long memberId) {
		return Optional.ofNullable(queryFactory
			.select(new QMemberProfileResponseDTO(
				memberEntity.name,
				memberEntity.profileUrl,
				companyEntity.name,
				deptEntity.name,
				memberEntity.position
			))
			.from(memberEntity)
			.leftJoin(deptEntity).on(memberEntity.dept.id.eq(deptEntity.id)).fetchJoin()
			.leftJoin(companyEntity).on(deptEntity.company.id.eq(companyEntity.id)).fetchJoin()
			.distinct()
			.where(memberEntity.id.eq(memberId))
			.fetchOne());
	}
}
