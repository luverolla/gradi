package io.luverolla.gradi.services;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import static java.util.Map.entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import io.luverolla.gradi.comparators.*;
import io.luverolla.gradi.filters.*;
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

	private static final Map<String, EntityComparator<User>> CMPTS = Map.ofEntries(
        entry("code", new EntityComparatorCode<>()),
        entry("name", new UserComparatorName()),
        entry("surname", new UserComparatorSurname()),
        entry("email", new UserComparatorSurname()),
        entry("permissions", new UserComparatorPermissions()),
        entry("role", new UserComparatorRole()),
        entry("createdAt", new EntityComparatorCreatedAt<>()),
        entry("updatedAt", new EntityComparatorUpdatedAt<>())
    );
    
    private static final Map<String, EntityFilter<User, ?>> FLTRS = Map.ofEntries(
        entry("code", new EntityFilterCode<>()),
        entry("name", new UserFilterName()),
        entry("surname", new UserFilterSurname()),
        entry("email", new UserFilterEmail()),
        entry("role", new UserFilterRole()),
        entry("createdAt", new EntityFilterCreatedAt<>()),
        entry("updatedAt", new EntityFilterUpdatedAt<>())
    );

    @Override
    protected Map<String, EntityComparator<User>> getComparatorMap()
    {
    	return CMPTS;
    }
    
    @Override
    protected Map<String, EntityFilter<User, ?>> getFilterMap()
    {
    	return FLTRS;
    }
    
    @Override
    protected JpaRepository<User, Long> repo()
    {
    	return repo;
    }
    
    @Override
    public SortedSet<User> get(EntitySetRequest<User> req)
    {
    	SortedSet<User> data = super.get(req);
    	
    	// we don't want the admin data to be shows as normal users
    	data.removeIf(u -> u.getCode().equals("00000"));
    	return data;
    }
    
    @Override
    public User get(String code)
    {
    	// we don't want the admin data to be shows as normal users
    	return code.equals("00000") ? null : super.get(code);
    }

    @Override
    public Set<User> add(Set<User> src)
    {
        return new HashSet<>(repo.saveAll(src));
    }

    @Override
    public User update(String code, User data)
    {
        User found = repo.findAll().stream()
            .filter(u -> u.getCode().equals(code))
                .findFirst().orElseThrow();

        found.setName(data.getName());
        found.setSurname(data.getSurname());
        found.setEmail(data.getEmail());
        found.setDescription(data.getDescription());
        found.setPassword(new BCryptPasswordEncoder().encode(data.getPassword()));
        found.setRole(data.getRole());
        found.setPermissions(data.getPermissions());

        return repo.save(found);
    }

    @Override
    public void delete(String code)
    {
        User found = repo.findAll().stream()
            .filter(u -> u.getCode().equals(code))
                .findFirst().orElseThrow();

        repo.delete(found);
    }
}
