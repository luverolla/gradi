package io.luverolla.gradi.entities;

import io.luverolla.gradi.structures.DatedEntity;

import javax.persistence.*;

@Entity
@Table(name = "permission")
public class ResourcePermission implements DatedEntity {
    @EmbeddedId
    private ResourcePermissionId id;

    @MapsId("resource")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resource", nullable = false)
    private Resource resource;

    @MapsId("user")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "\"user\"", nullable = false)
    private User user;

    @Column(name = "created_at", nullable = false)
    private java.time.OffsetDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private java.time.OffsetDateTime updatedAt;

    @Column(name = "type", columnDefinition = "permission_type(9) not null")
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ResourcePermissionId getId() {
        return id;
    }

    public void setId(ResourcePermissionId id) {
        this.id = id;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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