package io.luverolla.gradi.entities;

import io.luverolla.gradi.structures.CodedEntity;
import io.luverolla.gradi.structures.DatedEntity;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "type")
public class ResourceType implements CodedEntity, DatedEntity {
    @Id
    @Column(name = "code", nullable = false)
    private java.lang.Integer code;

    @Lob
    @Column(name = "name", nullable = false)
    private java.lang.String name;

    @Column(name = "description", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private java.lang.String description;

    @Column(name = "created_at", nullable = false)
    private java.time.OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private java.time.OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "type")
    private java.util.Set<io.luverolla.gradi.entities.ResourceProperty> properties = new java.util.LinkedHashSet<>();

    @OneToMany(mappedBy = "type")
    private java.util.Set<Resource> resources = new java.util.LinkedHashSet<>();

    public java.lang.Integer getCode() {
        return code;
    }

    public void setCode(java.lang.Integer code) {
        this.code = code;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
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

    public java.util.Set<io.luverolla.gradi.entities.ResourceProperty> getProperties() {
        return properties;
    }

    public void setProperties(java.util.Set<io.luverolla.gradi.entities.ResourceProperty> properties) {
        this.properties = properties;
    }

    public java.util.Set<Resource> getResources() {
        return resources;
    }

    public void setResources(java.util.Set<Resource> resources) {
        this.resources = resources;
    }

}