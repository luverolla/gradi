package io.luverolla.gradi.entities;

import io.luverolla.gradi.structures.CodedEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * Custom resource property of given backing class. This last one MUST be comparable
 *
 * <p>Resource properties can be dynamically created with custom types and related backing classes:</p>
 * <ul>
 *     <li><code>TEXT</code> is used for string-like properties. Backed by {@link String} class</li>
 *     <li><code>NUMERIC</code> is used for numeric properties. Backed by {@link Float} class, even if integer</li>
 *     <li><code>DATETIME</code> is used for dates and times. Backed by {@link java.time.OffsetDateTime} class</li>
 *     <li><code>BOOLEAN</code> is used for boolean-like properties. Backed by {@link Boolean} class</li>
 *     <li>
 *         <code>FIXED</code> is used for properties that accept a set of fixed values
 *         (as happens in HTML <code>select</code> menus. Backed by {@link Set<String>} class
 *     </li>
 *     <li>
 *         <code>RESOURCE</code> is used for properties that accept a set of {@link Resource} URIs.
 *         Backed by {@link Set<String>} class
 *     </li>
 * </ul>
 *
 * @see ResourceAttribute
 */
@Entity
@Table(name = "gradi_resource_properties")
@Getter
@Setter
@NoArgsConstructor
public class ResourceProperty extends CodedEntity
{
    public enum Type { TEXT, NUMERIC, DATETIME, BOOLEAN, FIXED, RESOURCE };

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Type type;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name="resource_type_code", nullable = false)
    private ResourceType resourceType;

    @OneToMany(mappedBy = "property", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<ResourceAttribute> attributes;

    @Override
    public boolean equals(Object o)
    {
        if(this == o)
            return true;

        if(o == null || getClass() != o.getClass())
            return false;

        ResourceProperty that = (ResourceProperty) o;
        return resourceType.equals(that.getResourceType()) && name.equals(that.getName());
    }
}
