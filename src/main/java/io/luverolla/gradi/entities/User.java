package io.luverolla.gradi.entities;

import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "\"user\"", indexes = {
        @Index(name = "user_email_idx", columnList = "email", unique = true)
})
public class User {
    @Id
    @Column(name = "code", nullable = false)
    private java.lang.Integer code;

    @Lob
    @Column(name = "name", nullable = false)
    private java.lang.String name;

    @Lob
    @Column(name = "surname", nullable = false)
    private java.lang.String surname;

    @Lob
    @Column(name = "email", nullable = false)
    private java.lang.String email;

    @Column(name = "created_at", nullable = false)
    private java.time.OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private java.time.OffsetDateTime updatedAt;

    @Column(name = "password", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private java.lang.String password;

    @ManyToMany
    @JoinTable(name = "recipient",
            joinColumns = @JoinColumn(name = "user"),
            inverseJoinColumns = @JoinColumn(name = "message"))
    private java.util.Set<Message> messages = new java.util.LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private java.util.Set<io.luverolla.gradi.entities.ResourcePermission> permissions = new java.util.LinkedHashSet<>();

    @Column(name = "role", columnDefinition = "user_role(12) not null")
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

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

    public java.lang.String getSurname() {
        return surname;
    }

    public void setSurname(java.lang.String surname) {
        this.surname = surname;
    }

    public java.lang.String getEmail() {
        return email;
    }

    public void setEmail(java.lang.String email) {
        this.email = email;
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

    public java.lang.String getPassword() {
        return password;
    }

    public void setPassword(java.lang.String password) {
        this.password = password;
    }

    public java.util.Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(java.util.Set<Message> messages) {
        this.messages = messages;
    }

    public java.util.Set<io.luverolla.gradi.entities.ResourcePermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(java.util.Set<io.luverolla.gradi.entities.ResourcePermission> permissions) {
        this.permissions = permissions;
    }

}