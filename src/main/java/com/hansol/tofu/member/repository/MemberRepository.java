package com.hansol.tofu.member.repository;

import com.hansol.tofu.member.domain.MemberEntity;
import com.hansol.tofu.member.enums.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    Optional<MemberEntity> findMemberByEmail(String email);

    Optional<MemberEntity> findMemberByEmailAndMemberStatus(String email, MemberStatus status);


    Optional<MemberEntity> findByIdAndMemberStatus(Long id, MemberStatus status);

    boolean existsMemberByEmail(String email);
}
