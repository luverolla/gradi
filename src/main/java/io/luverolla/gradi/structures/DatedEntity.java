package io.luverolla.gradi.structures;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import java.time.OffsetDateTime;

/**
 * Entity with fields that keep track of creation timestamp and last update timestamp
 */
@MappedSuperclass
@Getter
@Setter
public class DatedEntity extends BaseEntity
{
    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private OffsetDateTime updatedAt;
}
