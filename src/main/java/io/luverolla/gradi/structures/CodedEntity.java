package io.luverolla.gradi.structures;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;

import java.time.OffsetDateTime;

/**
 * Generic entity uniquely identified and sorted by a string code
 *
 * This code differs semantically from the ID because it must be mnemonic,
 * since it will be user by users to search for entities
 */
@MappedSuperclass
@Getter
@Setter
public class CodedEntity extends RepresentationModel<CodedEntity> implements DatedEntity, Comparable<CodedEntity>
{
    @Id
    @Column(nullable = false, unique = true, length = 10)
    private String code;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private OffsetDateTime updatedAt;

    /**
     * Two coded entity are equal if their codes are equals (ignoring case)
     * @param o the other entity
     *
     * @return boolean
     * 
     * @see String#equalsIgnoreCase(String)
     */
    @Override
    public boolean equals(Object o)
    {
        if(o == null)
            return false;

        if(getClass() != o.getClass())
            return false;

        if(this == o)
            return true;

        CodedEntity that = (CodedEntity)o;
        return code.equals(that.getCode());
    }

    /**
     * Comparison is made on codes ignoring case
     * @param o the other entity
     *
     * @return comparison result
     * 
     * @see String#compareToIgnoreCase(String)
     */
    @Override
    public int compareTo(CodedEntity o)
    {
        return code.compareToIgnoreCase(o.getCode());
    }
}
