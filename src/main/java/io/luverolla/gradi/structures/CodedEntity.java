package io.luverolla.gradi.structures;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
public class CodedEntity extends DatedEntity
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
}
