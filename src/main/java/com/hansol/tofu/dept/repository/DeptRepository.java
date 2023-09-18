package com.hansol.tofu.dept.repository;


import com.hansol.tofu.dept.domain.DeptEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeptRepository extends JpaRepository<DeptEntity, Long> {

    List<DeptEntity> findByCompanyId(Long companyId);
}
