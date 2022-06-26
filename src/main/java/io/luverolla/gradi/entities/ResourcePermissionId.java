package io.luverolla.gradi.entities;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.io.Serializable;

@Embeddable
public class ResourcePermissionId implements Serializable {
    private static final long serialVersionUID = -1L;
    @Column(name = "resource", nullable = false)
    private java.lang.Integer resource;

    @Column(name = "\"user\"", nullable = false)
    private java.lang.Integer user;

    public java.lang.Integer getResource() {
        return resource;
    }

    public void setResource(java.lang.Integer resource) {
        this.resource = resource;
    }

    public java.lang.Integer getUser() {
        return user;
    }

    public void setUser(java.lang.Integer user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ResourcePermissionId entity = (ResourcePermissionId) o;
        return java.util.Objects.equals(this.resource, entity.resource) &&
                java.util.Objects.equals(this.user, entity.user);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(resource, user);
    }

}