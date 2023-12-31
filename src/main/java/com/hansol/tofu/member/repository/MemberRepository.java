package com.hansol.tofu.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hansol.tofu.member.domain.MemberEntity;
import com.hansol.tofu.member.enums.MemberStatus;
import com.hansol.tofu.member.store.MemberQueryStore;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long>, MemberQueryStore {

	Optional<MemberEntity> findMemberByEmail(String email);

	Optional<MemberEntity> findMemberByEmailAndMemberStatus(String email, MemberStatus status);

	Optional<MemberEntity> findMemberById(Long id);

	Optional<MemberEntity> findMemberByIdAndMemberStatus(Long id, MemberStatus status);

	boolean existsMemberByEmail(String email);
}
