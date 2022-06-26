package io.luverolla.gradi.entities;

import io.luverolla.gradi.structures.DatedEntity;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "attribute")
public class ResourceAttribute implements DatedEntity {
    @EmbeddedId
    private ResourceAttributeId id;

    @MapsId("resource")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resource", nullable = false)
    private Resource resource;

    @MapsId
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns({
            @JoinColumn(name = "property_type", referencedColumnName = "type", nullable = false),
            @JoinColumn(name = "property_name", referencedColumnName = "name", nullable = false)
    })
    private ResourceProperty property;

    @Column(name = "value", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private java.lang.String value;

    @Column(name = "created_at", nullable = false)
    private java.time.OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private java.time.OffsetDateTime updatedAt;

    public ResourceAttributeId getId() {
        return id;
    }

    public void setId(ResourceAttributeId id) {
        this.id = id;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public ResourceProperty getProperty() {
        return property;
    }

    public void setProperty(ResourceProperty property) {
        this.property = property;
    }

    public java.lang.String getValue() {
        return value;
    }

    public void setValue(java.lang.String value) {
        this.value = value;
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