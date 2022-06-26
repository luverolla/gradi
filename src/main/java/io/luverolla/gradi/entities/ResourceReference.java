package io.luverolla.gradi.entities;

import io.luverolla.gradi.structures.DatedEntity;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "reference")
public class ResourceReference implements DatedEntity {
    @EmbeddedId
    private ResourceReferenceId id;

    @MapsId("resource")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resource", nullable = false)
    private Resource resource;

    @MapsId("referred")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "referred", nullable = false)
    private Resource referred;

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

    public ResourceReferenceId getId() {
        return id;
    }

    public void setId(ResourceReferenceId id) {
        this.id = id;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Resource getReferred() {
        return referred;
    }

    public void setReferred(Resource referred) {
        this.referred = referred;
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