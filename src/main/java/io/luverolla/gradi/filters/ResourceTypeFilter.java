package io.luverolla.gradi.filters;

import io.luverolla.gradi.entities.ResourceType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsoup.Jsoup;

import java.time.OffsetDateTime;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
public class ResourceTypeFilter
{
    private Optional<String> code;
    private Optional<String> name;
    private Optional<String> desc;
    private Optional<String> createdFrom;
    private Optional<String> createdTo;
    private Optional<String> updatedFrom;
    private Optional<String> updatedTo;

    public boolean test(ResourceType rt)
    {
        boolean match = true;

        if(code.isPresent() && code.get().length() > 0)
            match = match && rt.getCode().equalsIgnoreCase(code.get());

        if(name.isPresent() && name.get().length() > 0)
            match = match && rt.getName().toLowerCase().contains(name.get().toLowerCase());

        if(desc.isPresent() && desc.get().length() > 0)
            match = match && Jsoup.parse(rt.getDescription()).text().toLowerCase().contains(desc.get().toLowerCase());

        if(createdFrom.isPresent() && createdFrom.get().length() > 0)
            match = match && rt.getCreatedAt().isAfter(OffsetDateTime.parse(createdFrom.get()));

        if(createdTo.isPresent() && createdTo.get().length() > 0)
            match = match && rt.getCreatedAt().isBefore(OffsetDateTime.parse(createdTo.get()));

        if(updatedFrom.isPresent() && updatedFrom.get().length() > 0)
            match = match && rt.getUpdatedAt().isAfter(OffsetDateTime.parse(updatedFrom.get()));

        if(updatedTo.isPresent() && updatedTo.get().length() > 0)
            match = match && rt.getUpdatedAt().isBefore(OffsetDateTime.parse(updatedTo.get()));

        return match;
    }
}
