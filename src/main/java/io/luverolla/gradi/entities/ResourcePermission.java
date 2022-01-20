package io.luverolla.gradi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "gradi_resource_permissions")
@Getter
@Setter
@NoArgsConstructor
public class ResourcePermission implements DatedEntity
{
    public enum Type { READ, WRITE, FULL };

    @JsonIgnore
    @Id
    @GeneratedValue(generator = "gradi_resource_permission_id", strategy = GenerationType.SEQUENCE)
    private Long index;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private OffsetDateTime updatedAt;

    @JsonIgnoreProperties({"createdAt", "updatedAt", "description", "permissions"})
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_code", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "resource_code", nullable = false)
    private Resource resource;

    @Column
    @Enumerated(EnumType.ORDINAL)
    private Type type;

    @Override
    public boolean equals(Object o)
    {
        if(o == null || getClass() != o.getClass())
            return false;

        if(this == o)
            return true;

        ResourcePermission that = (ResourcePermission) o;
        return user.equals(that.getUser()) && resource.equals(that.getResource()) && type.equals(that.getType());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(user, resource);
    }
}
