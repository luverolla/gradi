package io.luverolla.gradi.entities;

import io.luverolla.gradi.structures.BaseEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ResourceAttribute extends BaseEntity
{
    @ManyToOne
    @JoinColumn(name="resource_id", nullable=false)
    private Resource resource;

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private ResourceProperty property;

    @ElementCollection
    @CollectionTable(name = "resource_values", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "values")
    private List<String> values;

    @Override
    public boolean equals(Object o)
    {
        if(this == o)
            return true;

        if(o == null || getClass() != o.getClass())
            return false;

        ResourceAttribute that = (ResourceAttribute)o;
        return resource.equals(that.getResource()) && property.equals(that.getProperty());
    }
}
