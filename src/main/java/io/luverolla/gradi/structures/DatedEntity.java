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
public interface DatedEntity
{
    OffsetDateTime getCreatedAt();
    OffsetDateTime getUpdatedAt();

    void setUpdatedAt(OffsetDateTime updatedAt);
}
