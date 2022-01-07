package io.luverolla.gradi.services;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import io.luverolla.gradi.comparators.*;
import io.luverolla.gradi.filters.*;
import io.luverolla.gradi.structures.CodedEntity;
import io.luverolla.gradi.structures.EntityComparator;
import io.luverolla.gradi.entities.User;
import io.luverolla.gradi.repositories.UserRepository;
import io.luverolla.gradi.rest.EntitySetRequest;
import io.luverolla.gradi.structures.EntityService;
import io.luverolla.gradi.structures.EntityFilter;

@Service
public class UserService extends EntityService<User>
{
    @Autowired
    private UserRepository repo;

    @Override
    protected Map<String, EntityComparator<User>> getComparatorMap()
    {
        return Map.ofEntries(
            Map.entry("code", new EntityComparatorCode<>()),
            Map.entry("name", new UserComparatorName()),
            Map.entry("surname", new UserComparatorSurname()),
            Map.entry("email", new UserComparatorSurname()),
            Map.entry("permissions", new UserComparatorPermissions()),
            Map.entry("role", new UserComparatorRole()),
            Map.entry("createdAt", new EntityComparatorCreatedAt<>()),
            Map.entry("updatedAt", new EntityComparatorUpdatedAt<>())
        );
    }
    
    @Override
    protected Map<String, EntityFilter<User, ?>> getFilterMap()
    {
    	return Map.ofEntries(
            Map.entry("code", new EntityFilterCode<>()),
            Map.entry("name", new UserFilterName()),
            Map.entry("surname", new UserFilterSurname()),
            Map.entry("email", new UserFilterEmail()),
            Map.entry("role", new UserFilterRole()),
            Map.entry("createdAt", new EntityFilterCreatedAt<>()),
            Map.entry("updatedAt", new EntityFilterUpdatedAt<>())
        );
    }

    public SortedSet<User> getUsers(EntitySetRequest<User> req)
    {
        Integer page = req.getPage(), lim = req.getLimit();
        boolean paging = page != null && lim != null;

        Collection<User> data = paging ? repo.findAll(page, lim) : repo.findAll();

        return data.stream().filter(r -> chainFilters(req).test(r))
            .collect(Collectors.toCollection(() -> new TreeSet<>(chainComparators(req))));
    }

    public User getOneUser(String code)
    {
        return repo.getById(code);
    }

    public Set<User> add(Set<User> src)
    {
        for(User u : src)
            u.setCode(CodedEntity.toBase36(5, (int)repo.count()));

        return src;
    }

    public User updateOneUser(String code, User data)
    {
        Optional<User> tg = repo.findById(code);

        if(tg.isPresent())
        {
            User found = tg.get();
            found.setName(data.getName());
            found.setSurname(data.getSurname());
            found.setEmail(data.getEmail());
            found.setDescription(data.getDescription());
            found.setPassword(new BCryptPasswordEncoder().encode(data.getPassword()));
            found.setRole(data.getRole());
            found.setPermissions(data.getPermissions());

            return repo.save(found);
        }

        return null;
    }
    
    public void deleteOneUser(String code)
    {
        repo.deleteOne(code);
    }
}
