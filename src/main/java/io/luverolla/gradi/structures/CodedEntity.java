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
    /**
     * UNIX timestamp (in milliseconds) of 2000-01-01T00:00:00+00:00
     */
    public static final long YEAR2000 = 946684800000L;

    /**
     * Converts number to base36 with up to <code>chars</code> digits
     *
     * Base36 extends Base16 adding the other 20 latin letters
     *
     * @param chars maximum number of digits
     * @param num number to convert
     *
     * @return converted string
     * @throws NoAvailableCodeException if converted number doesn't fit in given number of digits
     */
    public static String toBase36(int chars, long num)
    {
        if(num >= Math.pow(36, chars))
            throw new NoAvailableCodeException();

        String base36 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder bld = new StringBuilder(new String(new char[chars]).replace('\0', '0'));

        for(int i = chars - 1; i >= 0; i--)
        {
            if(num == 0) break;
            bld.setCharAt(i, base36.charAt( (int) (num % 36)) );
            num /= 36;
        }

        return bld.toString();
    }

    /**
     * Gets next unique code for entity
     *
     * @return unique code
     */
    public static String nextCode()
    {
        return toBase36(10, System.currentTimeMillis() - YEAR2000);
    }

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
