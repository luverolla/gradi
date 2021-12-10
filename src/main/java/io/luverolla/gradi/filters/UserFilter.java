package io.luverolla.gradi.filters;

import io.luverolla.gradi.entities.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsoup.Jsoup;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
public class UserFilter
{
    private Optional<String> code;
    private Optional<String> name;
    private Optional<String> surname;
    private Optional<String> email;
    private Optional<String> desc;
    private Optional<Set<String>> roles;
    private Optional<String> createdFrom;
    private Optional<String> createdTo;
    private Optional<String> updatedFrom;
    private Optional<String> updatedTo;

    public boolean test(User u)
    {
        boolean match = true;

        if(code.isPresent() && code.get().length() > 0)
            match = match && u.getCode().equalsIgnoreCase(code.get());

        if(name.isPresent() && name.get().length() > 0)
            match = match && u.getName().toLowerCase().contains(name.get().toLowerCase());

        if(surname.isPresent() && surname.get().length() > 0)
            match = match && u.getSurname().toLowerCase().contains(surname.get().toLowerCase());

        if(email.isPresent() && email.get().length() > 0)
            match = match && u.getEmail().toLowerCase().contains(email.get().toLowerCase());

        if(desc.isPresent() && desc.get().length() > 0)
            match = match && Jsoup.parse(u.getDescription()).text().toLowerCase().contains(desc.get().toLowerCase());

        if(roles.isPresent() && roles.get().size() > 0)
            match = match && roles.get().contains(u.getRole().toString());

        if(createdFrom.isPresent() && createdFrom.get().length() > 0)
            match = match && u.getCreatedAt().isAfter(OffsetDateTime.parse(createdFrom.get()));

        if(createdTo.isPresent() && createdTo.get().length() > 0)
            match = match && u.getCreatedAt().isBefore(OffsetDateTime.parse(createdTo.get()));

        if(updatedFrom.isPresent() && updatedFrom.get().length() > 0)
            match = match && u.getUpdatedAt().isAfter(OffsetDateTime.parse(updatedFrom.get()));

        if(updatedTo.isPresent() && updatedTo.get().length() > 0)
            match = match && u.getUpdatedAt().isBefore(OffsetDateTime.parse(updatedTo.get()));

        return match;
    }
}
