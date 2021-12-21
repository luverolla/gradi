package io.luverolla.gradi.structures;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Minimal entity with an unique generated ID
 */
@MappedSuperclass
@Setter
@Getter
public class BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Override
    public int hashCode()
    {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o)
            return true;

        if(o == null)
            return false;

        if(!getClass().equals(o.getClass()))
            return false;

        return id.equals( ((BaseEntity)o).getId() );
    }
}
