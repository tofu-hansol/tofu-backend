package com.hansol.tofu.dept.service;

import com.hansol.tofu.dept.domain.DeptResponseDTO;
import com.hansol.tofu.dept.repository.DeptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DeptService {

    private final DeptRepository deptRepository;

    public List<DeptResponseDTO> getDepts(Long companyId) {
        return deptRepository.findByCompanyId(companyId).stream().map(DeptResponseDTO::toDTO).collect(Collectors.toList());
    }
}
