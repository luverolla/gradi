package io.luverolla.gradi.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.luverolla.gradi.structures.DatedEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;

import java.time.OffsetDateTime;

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
public class ResourceAttribute implements DatedEntity
{
    @Id
    private String name;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private OffsetDateTime updatedAt;

    @Column(columnDefinition = "text")
    private String value;

    @JsonIgnoreProperties({"attributes", "type"})
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "resource_property_name", nullable = false)
    private ResourceProperty property;

    @JsonIgnoreProperties({"attributes"})
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "resource_code", nullable = false)
    private Resource resource;

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
}
