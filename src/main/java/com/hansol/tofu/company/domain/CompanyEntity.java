package com.hansol.tofu.company.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@EqualsAndHashCode(callSuper = false)
@Entity
@Builder
@Table(name = "company")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

}
