package io.luverolla.gradi.entities;

import io.luverolla.gradi.structures.BaseEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ResourceProperty extends BaseEntity
{
    public enum Type { TEXT, NUMERIC, DATETIME, BOOLEAN, FIXED };

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Type type;

    @ManyToOne
    @JoinColumn(name="resource_type_id", nullable = false)
    private ResourceType resourceType;

    @OneToMany(mappedBy = "property")
    private Set<ResourceAttribute> attributes;

    @Override
    public boolean equals(Object o)
    {
        if(this == o)
            return true;

        if(o == null || getClass() != o.getClass())
            return false;

        ResourceProperty that = (ResourceProperty)o;
        return resourceType.equals(that.getResourceType()) && name.equals(that.getName());
    }
}
