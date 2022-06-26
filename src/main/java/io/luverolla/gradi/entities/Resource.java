package io.luverolla.gradi.entities;

import io.luverolla.gradi.structures.CodedEntity;
import io.luverolla.gradi.structures.DatedEntity;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "resource")
public class Resource implements CodedEntity, DatedEntity {
    @Id
    @Column(name = "code", nullable = false)
    private java.lang.Integer code;

    @Lob
    @Column(name = "title", nullable = false)
    private java.lang.String title;

    @Column(name = "description", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private java.lang.String description;

    @Column(name = "created_at", nullable = false)
    private java.time.OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private java.time.OffsetDateTime updatedAt;

    @Column(name = "visibility", columnDefinition = "resource_visibility not null")
    private String visibility;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "type", nullable = false)
    private ResourceType type;

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public java.lang.Integer getCode() {
        return code;
    }

    public void setCode(java.lang.Integer code) {
        this.code = code;
    }

    public java.lang.String getTitle() {
        return title;
    }

    public void setTitle(java.lang.String title) {
        this.title = title;
    }

    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public java.time.OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.time.OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public java.time.OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(java.time.OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}