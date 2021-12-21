package io.luverolla.gradi.structures;

import lombok.Getter;
import lombok.Setter;
import org.aspectj.apache.bcel.classfile.Code;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Generic entity uniquely identified and sorted by a string code
 *
 * This code differs semantically from the ID because it must be mnemonic,
 * since it will be user by users to search for entities
 */
@MappedSuperclass
@Getter
@Setter
public class CodedEntity extends DatedEntity implements Comparable<CodedEntity>
{
    @Column(nullable = false, unique = true)
    private String code;

    @Override
    public boolean equals(Object o)
    {
        if(this == o)
            return true;

        if(o == null || getClass() != o.getClass())
            return false;

        CodedEntity that = (CodedEntity)o;
        return code.equals(that.getCode());
    }

    @Override
    public int compareTo(CodedEntity o)
    {
        return code.compareToIgnoreCase(o.getCode());
    }
}
