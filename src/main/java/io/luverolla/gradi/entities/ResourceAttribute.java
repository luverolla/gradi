package io.luverolla.gradi.entities;

import io.luverolla.gradi.structures.CodedEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Resource custom attribute
 *
 * <p>
 *     Here, <code>value</code> is expressed as a {@link String} regardless of the original data type.
 *     In case of single values, the relative <code>toString()</code> method, while, in case of a set of values
 *     (for FIXED and RESOURCE types), elements are put in a string separated by semicolon (;)
 * </p>
 */
@Entity
@Table(name = "gradi_resource_attributes")
@Getter
@Setter
@NoArgsConstructor
public class ResourceAttribute extends CodedEntity
{
    @Id
    private String name;

    @Column(columnDefinition = "text")
    private String value;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "resource_code", nullable = false)
    private Resource resource;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "resource_property_code", nullable = false)
    private ResourceProperty property;

    @Override
    public boolean equals(Object o)
    {
        if(this == o)
            return true;

        if(o == null || getClass() != o.getClass())
            return false;

        ResourceAttribute that = (ResourceAttribute) o;
        return resource.equals(that.getResource()) && property.equals(that.getProperty());
    }

    /**
     * Tells if resource attribute may belog to a given resource.
     *
     * @param r the given resource
     *
     * @return boolean
     */
    public boolean belongsTo(Resource r)
    {
        return property.getResourceType().equals(r.getType());
    }
}
