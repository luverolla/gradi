package io.luverolla.gradi.entities;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.io.Serializable;

@Embeddable
public class ResourceReferenceId implements Serializable {
    private static final long serialVersionUID = -1L;
    @Column(name = "resource", nullable = false)
    private java.lang.Integer resource;

    @Column(name = "referred", nullable = false)
    private java.lang.Integer referred;

    public java.lang.Integer getResource() {
        return resource;
    }

    public void setResource(java.lang.Integer resource) {
        this.resource = resource;
    }

    public java.lang.Integer getReferred() {
        return referred;
    }

    public void setReferred(java.lang.Integer referred) {
        this.referred = referred;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ResourceReferenceId entity = (ResourceReferenceId) o;
        return java.util.Objects.equals(this.resource, entity.resource) &&
                java.util.Objects.equals(this.referred, entity.referred);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(resource, referred);
    }

}