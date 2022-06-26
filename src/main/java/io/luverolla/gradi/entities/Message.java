package io.luverolla.gradi.entities;

import io.luverolla.gradi.structures.CodedEntity;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "message")
public class Message implements CodedEntity {
    @Id
    @Column(name = "code", nullable = false)
    private java.lang.Integer code;

    @Lob
    @Column(name = "subject", nullable = false)
    private java.lang.String subject;

    @Column(name = "datetime", nullable = false)
    private java.time.OffsetDateTime datetime;

    @Column(name = "text", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private java.lang.String text;

    @ManyToMany
    @JoinTable(name = "recipient",
            joinColumns = @JoinColumn(name = "message"),
            inverseJoinColumns = @JoinColumn(name = "user"))
    private java.util.Set<User> users = new java.util.LinkedHashSet<>();

    public java.lang.Integer getCode() {
        return code;
    }

    public void setCode(java.lang.Integer code) {
        this.code = code;
    }

    public java.lang.String getSubject() {
        return subject;
    }

    public void setSubject(java.lang.String subject) {
        this.subject = subject;
    }

    public java.time.OffsetDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(java.time.OffsetDateTime datetime) {
        this.datetime = datetime;
    }

    public java.lang.String getText() {
        return text;
    }

    public void setText(java.lang.String text) {
        this.text = text;
    }

    public java.util.Set<User> getUsers() {
        return users;
    }

    public void setUsers(java.util.Set<User> users) {
        this.users = users;
    }

}