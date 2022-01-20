package io.luverolla.gradi.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.luverolla.gradi.exceptions.ResourceTypeMismatchException;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

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

    @JsonIgnoreProperties({"resources"})
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "resource_type_code")
    private ResourceType type;
    
    @ManyToOne(fetch = FetchType.EAGER)
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
        return attributes.stream()
            .filter(a -> a.getProperty().equals(p))
                .findFirst().orElseThrow(ResourceTypeMismatchException::new);
    }

    public Set<User> getPermissionsUsers()
    {
        return permissions.stream()
            .map(ResourcePermission::getUser)
                .collect(Collectors.toSet());
    }

    public Set<ResourcePermission.Type> getPermissionsTypes()
    {
        return permissions.stream()
            .map(ResourcePermission::getType)
                .collect(Collectors.toSet());
    }

    public ResourcePermission.Type getPermissionType(User u)
    {
        ResourcePermission p = permissions.stream()
            .filter(e -> e.getUser().equals(u)).findFirst()
                .orElseThrow(NoSuchElementException::new);

        return p.getType();
    }
}
