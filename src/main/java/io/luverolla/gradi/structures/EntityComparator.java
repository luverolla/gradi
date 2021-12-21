package io.luverolla.gradi.structures;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Comparator;

/**
 * Entity comparator
 *
 * Extends standard Java comparator by adding possibility to compare in both ways: ascending or descending.
 * Setting the <code>desc</code> parameter to <code>true</code> allows sorting in descending order
 *
 * @param <E> entity type, derived from {@link BaseEntity}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class EntityComparator<E extends BaseEntity> implements Comparator<E>
{
    private Boolean desc = false;

    /**
     * Base comparing function. Must be implemented
     * 
     * It doesn't take in account the <code>desc</code> parameter value
     *
     * @see Comparator#compare
     */
    public abstract int apply(E o1, E o2);

    /**
     * Actual advanced comparing function, takes in account the sorting way
     *
     * Relies on abstract {@link EntityComparator#apply} method
     */
    @Override
    public int compare(E o1, E o2)
    {
        return (desc ? -1 : 1) * apply(o1, o2);
    }
}
