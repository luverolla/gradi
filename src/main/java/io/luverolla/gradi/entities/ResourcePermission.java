package io.luverolla.gradi.entities;

import io.luverolla.gradi.structures.DatedEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "gradi_resource_permissions")
@Getter
@Setter
@NoArgsConstructor
public class ResourcePermission extends DatedEntity
{
    public enum Type { READ, WRITE, FULL };

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name="resource_id", nullable=false)
    private Resource resource;

    @Column
    @Enumerated(EnumType.ORDINAL)
    private Type type;

    @Override
    public boolean equals(Object o)
    {
        if(this == o)
            return true;

        if(o == null || getClass() != o.getClass())
            return false;

        ResourcePermission that = (ResourcePermission)o;
        return resource.equals(that.getResource()) && user.equals(that.getUser());
    }
}
