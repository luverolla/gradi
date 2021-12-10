package io.luverolla.gradi.filters;

import io.luverolla.gradi.entities.Resource;
import io.luverolla.gradi.entities.ResourceAttribute;

import io.luverolla.gradi.entities.ResourcePermission;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.jsoup.Jsoup;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
public class ResourceFilter
{
    // common parameters
    private Optional<String> code;
    private Optional<String> name;
    private Optional<String> desc;
    private Optional<String> createdFrom;
    private Optional<String> createdTo;
    private Optional<String> updatedFrom;
    private Optional<String> updatedTo;
    private Optional<List<ResourcePermission>> permissions;

    /**
     * Custom parameters map
     *
     * key is property's name
     * value is a list, and has to be read as
     *  - singleton list: for TEXT and BOOLEAN types
     *   - resource property value must either be equal to or contain part of, the only element of list to match
     *  - two-element list: for NUMERIC and DATETIME
     *   - element at position 0 is the lower bound (LB)
     *   - element at position 1 is the upper bound (UB)
     *   - if one of them is null, then it's not considered
     *   - resource property value (RV) must be inside ]LB, UB[ interval to match
     *  - normal list: for FIXED type
     *   - resource property values list must contain all elements to match
     */
    private Optional<Map<String, List<String>>> custom;

    public boolean test(Resource e)
    {
        boolean match = true;

        if(code.isPresent() && code.get().length() > 0)
            match = match && e.getCode().equalsIgnoreCase(code.get());

        if(name.isPresent() && name.get().length() > 0)
            match = match && e.getName().toLowerCase().contains(name.get().toLowerCase());

        if(desc.isPresent() && desc.get().length() > 0)
            match = match && Jsoup.parse(e.getDescription()).text().toLowerCase().contains(desc.get());

        if(createdFrom.isPresent() && createdFrom.get().length() > 0)
            match = match && e.getCreatedAt().isAfter(OffsetDateTime.parse(createdFrom.get()));

        if(createdTo.isPresent() && createdTo.get().length() > 0)
            match = match && e.getCreatedAt().isBefore(OffsetDateTime.parse(createdTo.get()));

        if(updatedFrom.isPresent() && updatedFrom.get().length() > 0)
            match = match && e.getUpdatedAt().isAfter(OffsetDateTime.parse(updatedFrom.get()));

        if(updatedTo.isPresent() && updatedTo.get().length() > 0)
            match = match && e.getUpdatedAt().isBefore(OffsetDateTime.parse(updatedTo.get()));

        if(permissions.isPresent() && permissions.get().size() > 0)
            match = match && e.getPermissions().containsAll(permissions.get());

        if(custom.isPresent() && custom.get().size() > 0)
        {
            Map<String, List<String>> actual = custom.get();

            for(ResourceAttribute ra : e.getAttributes())
            {
                String fval, from, to;
                String prop = ra.getProperty().getName();

                boolean cmatch = true;

                switch(ra.getProperty().getType())
                {
                    case TEXT:
                        fval = actual.get(prop).get(0).toLowerCase();
                        cmatch = ra.getValues().get(0).toLowerCase().contains(fval);
                        break;

                    case BOOLEAN:
                        fval = actual.get(prop).get(0).toLowerCase();
                        cmatch = Boolean.valueOf(ra.getValues().get(0)).equals(Boolean.valueOf(fval));
                        break;

                    case FIXED:
                        cmatch = ra.getValues().containsAll(actual.get(prop));
                        break;

                    // this uses float, but good for integer too (integers can be seen as floats with zero-values decimal digits)
                    case NUMERIC:
                        from = actual.get(prop).get(0);
                        to = actual.get(prop).get(1);

                        float nval = Float.parseFloat(ra.getValues().get(0));
                        if(from != null) cmatch = cmatch && nval > Float.parseFloat(from);
                        if(to != null)   cmatch = cmatch && nval < Float.parseFloat(to);
                        break;

                    // case DATETIME:
                    default:
                        from = actual.get(prop).get(0);
                        to = actual.get(prop).get(1);

                        OffsetDateTime dval = OffsetDateTime.parse(ra.getValues().get(0));
                        if(from != null) cmatch = cmatch && dval.isAfter(OffsetDateTime.parse(from));
                        if(to != null)   cmatch = cmatch && dval.isBefore(OffsetDateTime.parse(to));
                        break;
                }

                match = match && cmatch;
            }
        }

        return match;
    }
}
