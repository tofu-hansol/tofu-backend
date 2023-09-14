package com.hansol.tofu.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hansol.tofu.member.domain.MemberEntity;
import com.hansol.tofu.member.enums.MemberStatus;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

	Optional<MemberEntity> findMemberByEmailAndMemberStatus(String email, MemberStatus status);

	Optional<MemberEntity> findMemberByEmail(String email);

	Optional<MemberEntity> findByIdAndMemberStatus(Long id, MemberStatus status);

	boolean existsMemberByEmail(String email);
}
