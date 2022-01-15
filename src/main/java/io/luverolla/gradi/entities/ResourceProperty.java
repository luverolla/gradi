package io.luverolla.gradi.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.luverolla.gradi.structures.CodedEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.util.Set;

/**
 * Custom resource property of given backing class. This last one MUST be comparable
 *
 * @see ResourceAttribute
 */
@Entity
@Table(name = "gradi_resource_properties")
@Getter
@Setter
@NoArgsConstructor
public class ResourceProperty extends RepresentationModel<ResourceProperty>
{
    /**
     * Property's value type
     *
     * <ul>
     *     <li><code>STRING</code> is for single-line, plain-text values, while <code>TEXT</code> is for formatted multiline text</li>
     *     <li><code>FIXED</code> is for enum-like values, or, for example, string values that comes from an HTML select</li>
     *     <li><code>RESOURCE</code> is for internal reference with resources' URIs</li>
     *     <li><code>NUMERIC</code></li>
     * </ul>
     */
    public enum Type { STRING, TEXT, NUMERIC, DATETIME, BOOLEAN, FIXED, RESOURCE };

    @Id
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Type type;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name="resource_type_code", nullable = false)
    private ResourceType resourceType;

    @JsonIgnoreProperties({"property"})
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
