package io.luverolla.gradi.structures;

import java.time.OffsetDateTime;

/**
 * Entity with fields that keep track of creation timestamp and last update timestamp
 *
 * Needed to create generic createdAt and updatedAt filters
 */
public interface DatedEntity
{
    OffsetDateTime getCreatedAt();
    OffsetDateTime getUpdatedAt();
}