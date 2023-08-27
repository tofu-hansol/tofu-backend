package com.hansol.tofu.global;

import java.time.ZonedDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;


@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class TimeEntity {

    @CreationTimestamp
    @Column(name = "created_time")
    private ZonedDateTime createdTime;

    @UpdateTimestamp
    @Column(name = "updated_time")
    private ZonedDateTime updatedTime;

}
