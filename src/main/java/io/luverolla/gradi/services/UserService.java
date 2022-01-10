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

    @Autowired
    private MailingService mailingService;

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

    public Set<User> getAllUsers()
    {
        return new HashSet<>(repo.findAll());
    }

    public Set<User> getUsersByRole(User.Role role)
    {
        return repo.findAllByRole(role);
    }

    public void passwordReset(User u)
    {
        StringBuilder bld = new StringBuilder(16);
        new Random().ints().limit(16).forEach(bld::append);

        // actually set password AFTER sending email, so,
        // if email send goes into error, password doesn't change
        // without email, user will never know its new new password
        String pswd = bld.toString();
        mailingService.notifyPasswordChange(u, pswd);
        u.setPassword(new BCryptPasswordEncoder().encode(pswd));
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

    public User getAdmin()
    {
        return getOneUser("0000000000");
    }

    public Set<User> add(Set<User> src)
    {
        for(User u : src)
            u.setCode(CodedEntity.nextCode());

        return src;
    }

    public User updateOneUser(String code, User data)
    {
        Optional<User> tg = repo.findById(code);

        if(tg.isPresent())
        {
            User found = tg.get();

            // mail notification only if visible significant data changes
            // both old and new email address are contacted
            if( !data.getFullName().equals(found.getFullName()) ||
                !data.getEmail().equals(found.getEmail()) ||
                !data.getRole().equals(found.getRole())
            )
                mailingService.notifyUserUpdate(code, found.getEmail(), data);

            found.setName(data.getName());
            found.setSurname(data.getSurname());
            found.setEmail(data.getEmail());
            found.setDescription(data.getDescription());
            found.setPassword(data.getPassword());
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
