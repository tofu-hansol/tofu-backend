package com.hansol.tofu.company.service;

import com.hansol.tofu.company.domain.CompanyResponseDTO;
import com.hansol.tofu.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public List<CompanyResponseDTO> getCompanies() {
        return companyRepository.findAll().stream().map(CompanyResponseDTO::toDTO).collect(Collectors.toList());
    }
}
