package io.luverolla.gradi.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.luverolla.gradi.structures.CodedEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.Set;

@Entity
@Table(name = "gradi_resource_types")
@Getter
@Setter
@NoArgsConstructor
public class ResourceType extends CodedEntity
{
    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String brief;

    @Column(columnDefinition = "text")
    private String description;

    @JsonIgnoreProperties({"type"})
    @OneToMany(mappedBy = "type", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<Resource> resources;

    @JsonIgnoreProperties({"resourceType", "attributes"})
    @OneToMany(mappedBy = "resourceType", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<ResourceProperty> properties;
}
