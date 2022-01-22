package io.luverolla.gradi.entities;

import io.luverolla.gradi.structures.CodedEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Table(name = "gradi_messages")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message extends CodedEntity
{
    public enum Visibility { GLOBAL, EDITORS, PRIVATE };

    public enum Type { GENERAL, INFO, WARNING, SECURITY };

    @Column
    private String subject;

    @Column
    @Enumerated(EnumType.ORDINAL)
    private Visibility visibility;

    @Column
    @Enumerated(EnumType.ORDINAL)
    private Type type;

    @Column(columnDefinition = "text")
    private String text;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
        name = "gradi_messages_recipients",
        joinColumns = { @JoinColumn(name = "user_code") },
        inverseJoinColumns = { @JoinColumn(name = "message_code") }
    )
    private Set<User> recipients;
}
