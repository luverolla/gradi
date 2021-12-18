package io.luverolla.gradi.entities;

import io.luverolla.gradi.structures.CodedEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.Set;

@Entity
@Table(name = "gradi_users")
@Getter
@Setter
@NoArgsConstructor
public class User extends CodedEntity
{
    public enum Role { USER, EDITOR, ADMIN };

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<ResourcePermission> permissions;

    public String getFullName()
    {
        return name + " " + surname;
    }

    public String getRecipientName()
    {
        return getFullName() + " <" + email + ">";
    }
    
    public String getURI()
    {
    	return "/users/" + getCode();
    }
}
