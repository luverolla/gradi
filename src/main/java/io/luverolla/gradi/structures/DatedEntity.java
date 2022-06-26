package io.luverolla.gradi.structures;

import java.time.OffsetDateTime;

public interface DatedEntity {
    OffsetDateTime getCreatedAt();
    OffsetDateTime getUpdatedAt();
}
