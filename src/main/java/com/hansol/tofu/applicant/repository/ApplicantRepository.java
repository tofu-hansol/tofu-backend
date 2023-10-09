package com.hansol.tofu.applicant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hansol.tofu.applicant.domain.ApplicantEntity;

public interface ApplicantRepository extends JpaRepository<ApplicantEntity, Long> {
}
