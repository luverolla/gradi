package io.luverolla.gradi.entities;

import io.luverolla.gradi.structures.CodedEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ResourceType extends CodedEntity
{
    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @OneToMany(mappedBy = "type")
    private Set<Resource> resources;

    @OneToMany(mappedBy = "resourceType")
    private Set<ResourceProperty> properties;
}
