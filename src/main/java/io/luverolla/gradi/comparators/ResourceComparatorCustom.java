package io.luverolla.gradi.comparators;

import io.luverolla.gradi.entities.Resource;
import io.luverolla.gradi.entities.ResourceProperty;
import io.luverolla.gradi.exceptions.ResourceTypeMismatchException;
import io.luverolla.gradi.structures.EntityComparator;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

/**
 * Compares resources by custom property
 */
@Getter
@Setter
public class ResourceComparatorCustom<T extends Comparable<T>> extends EntityComparator<Resource>
{
    private final ResourceProperty property;

    public ResourceComparatorCustom(ResourceProperty property, Boolean desc)
    {
        this.property = property;
        setDesc(desc);
    }

    @Override
    public int apply(Resource o1, Resource o2)
    {
        if(!o1.getType().equals(o2.getType()))
            throw new ResourceTypeMismatchException();

        String attr1 = o1.getAttribute(property).getValue();
        String attr2 = o2.getAttribute(property).getValue();

        switch(property.getType())
        {
            case TEXT:
                return attr1.compareToIgnoreCase(attr2);

            case FIXED: case RESOURCE:
                String[] values1 = attr1.split(";");
                String[] values2 = attr2.split(";");

                if(values1.length != values2.length)
                    return values1.length - values2.length;

                for(int i = 0; i < values1.length; i++)
                {
                    int compared = values1[i].compareToIgnoreCase(values2[i]);
                    if(compared != 0)
                        return compared;
                }
                return 0;

            case BOOLEAN:
                return Boolean.valueOf(attr1).compareTo(Boolean.valueOf(attr2));

            case NUMERIC:
                return OffsetDateTime.parse(attr1).compareTo(OffsetDateTime.parse(attr2));
        }

        return 0;
    }
}
