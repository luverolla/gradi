package io.luverolla.gradi.entities;

import io.luverolla.gradi.structures.CodedEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Resource extends CodedEntity
{
    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @ManyToOne
    @JoinColumn(name = "resource_id", nullable = false)
    private ResourceType type;

    @OneToMany(mappedBy = "resource")
    private Set<ResourceAttribute> attributes;

    @OneToMany(mappedBy = "resource")
    private Set<ResourcePermission> permissions;
}
