package io.luverolla.gradi.entities;

import io.luverolla.gradi.structures.CodedEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
    private String description;

    @OneToMany(mappedBy = "type", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<Resource> resources;

    @OneToMany(mappedBy = "resourceType", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<ResourceProperty> properties;
    
    public String getURI()
    {
    	return "/types/" + getCode();
    }
}
