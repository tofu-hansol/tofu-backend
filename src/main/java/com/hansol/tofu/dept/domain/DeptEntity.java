package com.hansol.tofu.dept.domain;

import com.hansol.tofu.company.domain.CompanyEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@EqualsAndHashCode(callSuper = false)
@Entity
@Builder
@Table(name = "dept")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DeptEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @JoinColumn(name = "company_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CompanyEntity company;

}
