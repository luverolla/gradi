package io.luverolla.gradi.structures;

import java.util.Comparator;
import java.util.List;

public class ChainedComparator<E extends BaseEntity> implements Comparator<E>
{
    private List<Comparator<E>> comparators;

    @SafeVarargs
    public ChainedComparator(Comparator<E>... cmpts)
    {
        comparators = List.of(cmpts);
    }

    @Override
    public int compare(E o1, E o2)
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
