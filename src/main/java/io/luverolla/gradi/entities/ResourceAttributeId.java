package io.luverolla.gradi.entities;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;
import java.io.Serializable;

@Embeddable
public class ResourceAttributeId implements Serializable {
    private static final long serialVersionUID = -1L;
    @Column(name = "resource", nullable = false)
    private java.lang.Integer resource;

    @Column(name = "property_type", nullable = false)
    private java.lang.Integer propertyType;

    @Lob
    @Column(name = "property_name", nullable = false)
    private java.lang.String propertyName;

    @Column(name = "number", nullable = false)
    private java.lang.Integer number;

    public java.lang.Integer getResource() {
        return resource;
    }

    public void setResource(java.lang.Integer resource) {
        this.resource = resource;
    }

    public java.lang.Integer getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(java.lang.Integer propertyType) {
        this.propertyType = propertyType;
    }

    public java.lang.String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(java.lang.String propertyName) {
        this.propertyName = propertyName;
    }

    public java.lang.Integer getNumber() {
        return number;
    }

    public void setNumber(java.lang.Integer number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ResourceAttributeId entity = (ResourceAttributeId) o;
        return java.util.Objects.equals(this.number, entity.number) &&
                java.util.Objects.equals(this.resource, entity.resource) &&
                java.util.Objects.equals(this.propertyName, entity.propertyName) &&
                java.util.Objects.equals(this.propertyType, entity.propertyType);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(number, resource, propertyName, propertyType);
    }

}