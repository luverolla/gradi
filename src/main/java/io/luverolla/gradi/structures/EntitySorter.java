package io.luverolla.gradi.structures;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntitySorter<E extends BaseEntity>
{
    private Comparator<E> cmpt;
    private boolean desc;

    public EntitySorter(Comparator<E> cmpt, boolean desc)
    {
        this.cmpt = cmpt;
        this.desc = desc;
    }

    public EntitySorter(Comparator<E> cmpt)
    {
        this.cmpt = cmpt;
        this.desc = false;
    }

    public Collection<E> sort(Collection<E> src)
    {
        Comparator<E> sortCmpt = (E o1, E o2) -> (desc ? -1 : 1) * cmpt.compare(o1, o2);
        return new TreeSet<>(sortCmpt);
    }
}
