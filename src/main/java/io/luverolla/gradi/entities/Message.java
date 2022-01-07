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

    @Column(nullable = false)
    @GeneratedValue(generator = "gradi_message_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "gradi_message_sequence", sequenceName = "gradi_message_sequence")
    private Long index;

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

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "sender_code", nullable = false)
    private User sender;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<User> recipients;
}
