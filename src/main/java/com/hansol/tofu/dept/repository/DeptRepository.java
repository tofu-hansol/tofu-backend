package com.hansol.tofu.dept.repository;


import com.hansol.tofu.company.domain.CompanyEntity;
import com.hansol.tofu.dept.domain.DeptEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeptRepository extends JpaRepository<DeptEntity, Long> {
}
