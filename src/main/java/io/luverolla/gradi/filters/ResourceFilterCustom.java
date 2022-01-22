package io.luverolla.gradi.filters;

import io.luverolla.gradi.entities.Resource;
import io.luverolla.gradi.entities.ResourceProperty;

import io.luverolla.gradi.structures.Filter;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

/**
 * Filter resources by custom property
 *
 * <p>According to data type, different "filter questions" are considered:</p>
 * <ul>
 *     <li>For <code>TEXTUAL</code> properties: values contain substring (ignoring case)?</li>
 *     <li>For <code>FIXED</code> or <code>RESOURCE</code>: values are equal to one, or more, from a provided set? </li>
 *     <li>For <code>BOOLEAN</code>: value is equal to provided one? (allMatch() is used even if set is singleton)</li>
 *     <li>For <code>NUMERIC</code> and <code>DATETIME</code>: value is in a provided range (boundaries included)?</li>
 * </ul>
 *
 * @see ResourceProperty
 */
@Getter
@Setter
public class ResourceFilterCustom extends Filter<Resource, Object>
{
    private ResourceProperty property;

    public ResourceFilterCustom(ResourceProperty property, Object value)
    {
        this.property = property;
        setValue(value);
    }

    @Override
    public boolean test(Resource entity)
    {
        String attr = entity.getAttribute(property).getValue().trim().toLowerCase();

        switch(property.getType())
        {
            case TEXT:
                String providedStr = (String) getValue();
                return attr.contains(providedStr.toLowerCase());

            case FIXED: case RESOURCE:
                Set<String> attrVals = Set.of(attr.split(";"));
                Set<String> providedVals = (Set<String>) getValue();
                return attrVals.stream().anyMatch(providedVals::contains);

            case BOOLEAN:
                boolean attrBool = Boolean.parseBoolean(attr);
                boolean providedBool = (Boolean) getValue();
                return attrBool == providedBool;

            case NUMERIC:
                Double attrNum = Double.parseDouble(attr);
                List<Double> bounds = (List<Double>) getValue();

                boolean cond = true;
                if(bounds.get(0) != null)
                    cond = attrNum.compareTo(bounds.get(0)) >= 0;
                if(bounds.get(1) != null)
                    cond = cond && attrNum.compareTo(bounds.get(1)) <= 0;

                return cond;

            case DATETIME:
                OffsetDateTime attrDate = OffsetDateTime.parse(attr);
                List<OffsetDateTime> dates = (List<OffsetDateTime>) getValue();

                cond = true;
                if(dates.get(0) != null)
                    cond = attrDate.compareTo(dates.get(0)) >= 0;
                if(dates.get(1) != null)
                    cond = cond && attrDate.compareTo(dates.get(1)) <= 0;

                return cond;
        }

        return false;
    }
}
