package io.luverolla.gradi.entities;

import javax.persistence.*;

@Entity
@Table(name = "recipient")
public class MessageRecipient {
    @EmbeddedId
    private MessageRecipientId id;

    @MapsId("message")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "message", nullable = false)
    private Message message;

    @MapsId("user")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "\"user\"", nullable = false)
    private User user;

    public MessageRecipientId getId() {
        return id;
    }

    public void setId(MessageRecipientId id) {
        this.id = id;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}