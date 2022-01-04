package io.luverolla.gradi.structures;

import java.util.Collection;
import java.util.Comparator;

/**
 * Comparator created by combination of other comparators for the same entity type
 * Sorting way is already taken in account by constituting comparators
 *
 * @param <E> entity type
 */
public class ChainedComparator<E> extends EntityComparator<E>
{
    private final Collection<Comparator<E>> comparators;
    
    public ChainedComparator(Collection<Comparator<E>> cmpts)
    {
    	comparators = cmpts;
    }

    @Override
    public int apply(E o1, E o2)
    {
        for(Comparator<E> c : comparators)
        {
            int res = c.compare(o1, o2);

            if(res != 0)
                return res;
        }

        return 0;
    }
}
