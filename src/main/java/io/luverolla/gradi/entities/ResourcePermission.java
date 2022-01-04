package io.luverolla.gradi.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "gradi_resource_permissions")
@Getter
@Setter
@NoArgsConstructor
public class ResourcePermission
{
    public enum Type { READ, WRITE, FULL };

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ResourcePermissionKey implements Serializable
    {
        @Column(name = "user_code")
        String userCode;

        @Column(name = "course_code")
        String resourceCode;

        @Override
        public boolean equals(Object o)
        {
            if(this == o)
                return true;
            if(o == null || getClass() != o.getClass())
                return false;

            ResourcePermissionKey that = (ResourcePermissionKey) o;
            return userCode.equals(that.userCode) && resourceCode.equals(that.resourceCode);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(userCode, resourceCode);
        }
    }

    @EmbeddedId
    ResourcePermissionKey code;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_code", nullable = false)
    @MapsId("userCode")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "resource_code", nullable = false)
    @MapsId("resourceCode")
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
        return code.equals(that.getCode());
    }

    @Override
    public int hashCode()
    {
        return code.hashCode();
    }
}
