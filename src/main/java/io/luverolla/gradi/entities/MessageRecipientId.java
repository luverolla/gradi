package io.luverolla.gradi.entities;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.io.Serializable;

@Embeddable
public class MessageRecipientId implements Serializable {
    private static final long serialVersionUID = -1L;
    @Column(name = "message", nullable = false)
    private java.lang.Integer message;

    @Column(name = "\"user\"", nullable = false)
    private java.lang.Integer user;

    public java.lang.Integer getMessage() {
        return message;
    }

    public void setMessage(java.lang.Integer message) {
        this.message = message;
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
        MessageRecipientId entity = (MessageRecipientId) o;
        return java.util.Objects.equals(this.message, entity.message) &&
                java.util.Objects.equals(this.user, entity.user);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(message, user);
    }

}