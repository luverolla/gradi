package io.luverolla.gradi.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.luverolla.gradi.structures.CodedEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "gradi_resources")
@Getter
@Setter
@NoArgsConstructor
public class Resource extends CodedEntity
{
    public enum Visibility { PUBLIC, INTERNAL, RESTRICTED };

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Visibility visibility;

    @Column(columnDefinition = "text")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "resource_type_code")
    private ResourceType type;
    
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "parent_resource_code")
    private Resource parent;

    @JsonIgnoreProperties({"parent"})
    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<Resource> children;

    @JsonIgnoreProperties({"resource"})
    @OneToMany(mappedBy = "resource", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<ResourceFile> files;

    @JsonIgnoreProperties({"resource"})
    @OneToMany(mappedBy = "resource", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<ResourceAttribute> attributes;

    @JsonIgnoreProperties({"resource"})
    @OneToMany(mappedBy = "resource", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<ResourcePermission> permissions;

    public ResourceAttribute getAttribute(ResourceProperty p)
    {
        if(!type.getProperties().contains(p))
            throw new RuntimeException("Resource type and property don't match");

        return attributes.stream()
            .filter(a -> a.getProperty().equals(p))
                .findFirst().orElse(null);
    }
}
