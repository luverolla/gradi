package io.luverolla.gradi.entities;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Lob;
import java.io.Serializable;

@Embeddable
public class ResourcePropertyId implements Serializable {
    private static final long serialVersionUID = -1L;
    @Column(name = "type", nullable = false)
    private java.lang.Integer type;

    @Lob
    @Column(name = "name", nullable = false)
    private java.lang.String name;

    public java.lang.Integer getType() {
        return type;
    }

    public void setType(java.lang.Integer type) {
        this.type = type;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ResourcePropertyId entity = (ResourcePropertyId) o;
        return java.util.Objects.equals(this.name, entity.name) &&
                java.util.Objects.equals(this.type, entity.type);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(name, type);
    }

}