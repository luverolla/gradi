package io.luverolla.gradi.entities;

import io.luverolla.gradi.structures.DatedEntity;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "property")
public class ResourceProperty implements DatedEntity {
    @EmbeddedId
    private ResourcePropertyId id;

    @Column(name = "created_at", nullable = false)
    private java.time.OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private java.time.OffsetDateTime updatedAt;
    @Column(name = "description", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private java.lang.String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "type", nullable = false)
    private ResourceType type;

    @Column(name = "data_type", columnDefinition = "property_type not null")
    private String dataType;

    @OneToMany(mappedBy = "property")
    private Set<ResourceAttribute> attributes = new LinkedHashSet<>();

    public Set<ResourceAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(Set<ResourceAttribute> attributes) {
        this.attributes = attributes;
    }

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public ResourcePropertyId getId() {
        return id;
    }

    public void setId(ResourcePropertyId id) {
        this.id = id;
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

    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

}