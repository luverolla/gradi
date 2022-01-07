package io.luverolla.gradi.structures;

import io.luverolla.gradi.exceptions.NoAvailableCodeException;

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
    public static String toBase36(int chars, int num)
    {
        if(num >= Math.pow(36, chars))
            throw new NoAvailableCodeException();

        String base36 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder bld = new StringBuilder("00000");

        for(int i = chars - 1; i >= 0; i--)
        {
            num = num / 36;

            bld.setCharAt(i, base36.charAt(num % 36));
            if(num == 0) break;
        }

        return bld.toString();
    }

    @Id
    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private OffsetDateTime updatedAt;

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

    @Override
    public int compareTo(CodedEntity o)
    {
        return code.compareToIgnoreCase(o.getCode());
    }
}
