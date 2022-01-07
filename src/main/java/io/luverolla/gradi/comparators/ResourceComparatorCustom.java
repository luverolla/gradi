package io.luverolla.gradi.comparators;

import io.luverolla.gradi.entities.Resource;
import io.luverolla.gradi.entities.ResourceProperty;
import io.luverolla.gradi.exceptions.InvalidPropertyException;
import io.luverolla.gradi.exceptions.ResourceTypeMismatchException;
import io.luverolla.gradi.structures.EntityComparator;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
        if(property == null)
            throw new InvalidPropertyException();

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
                String[] str1 = attr1.toLowerCase().split(";");
                String[] str2 = attr2.toLowerCase().split(";");

                Arrays.sort(str1);
                Arrays.sort(str2);

                // if the string weren't ordered, equal strings could be signaled as different
                // example: str1 = "a;b;c", str2 = "b;c;a"
                // they have all equal elements, but are different if not ordered
                return String.join(";", str1).compareTo(String.join(";", str2));

            case BOOLEAN:
                return Boolean.valueOf(attr1).compareTo(Boolean.valueOf(attr2));

            case NUMERIC:
                return Double.valueOf(attr1).compareTo(Double.valueOf(attr2));

            case DATETIME:
                return OffsetDateTime.parse(attr1).compareTo(OffsetDateTime.parse(attr2));
        }

        return 0;
    }
}
