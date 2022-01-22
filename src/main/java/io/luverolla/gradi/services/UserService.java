package io.luverolla.gradi.services;

import java.security.Principal;
import java.util.*;

import io.luverolla.gradi.structures.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.luverolla.gradi.comparators.*;
import io.luverolla.gradi.filters.*;
import io.luverolla.gradi.entities.User;
import io.luverolla.gradi.repositories.UserRepository;
import io.luverolla.gradi.structures.Filter;

@Service
public class UserService extends EntityService<User>
{
    @Autowired
    private UserRepository repo;

    @Autowired
    private MailingService mailingService;

    @Autowired
    private PasswordEncoder pwenc;

    @Override
    public PagingAndSortingRepository<User, String> repo()
    {
        return repo;
    }

    @Override
    protected Map<String, Comparator<User>> getComparatorMap()
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
    protected Map<String, Filter<User, ?>> getFilterMap()
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

    @Override
    public Set<User> add(Collection<User> data)
    {
        Set<User> saved = super.add(data);
        for(User u : saved)
            mailingService.notifyUserAdd(u);

        return saved;
    }

    @Override
    public User update(String code, User data)
    {
        Optional<User> tg = repo.findById(code);
        if(tg.isEmpty())
            throw new NoSuchElementException();

        User found = tg.get();

        // mail notification only if visible significant data changes
        // both old and new email address are contacted
        // recipientName contains name, surname and email address
        if( !data.getRecipientName().equals(found.getRecipientName()) ||
            !data.getRole().equals(found.getRole())
        )
            mailingService.notifyUserUpdate(code, found.getEmail(), data);

        found.setName(data.getName());
        found.setSurname(data.getSurname());
        found.setEmail(data.getEmail());
        found.setDescription(data.getDescription());

        return repo.save(found);
    }

    public User get(Principal pr)
    {
        return getByEmail(pr.getName());
    }

    public User getAdmin()
    {
        return repo.getAdmin();
    }

    public User getByEmail(String email)
    {
        return repo.findByEmail(email)
            .orElseThrow(NoSuchElementException::new);
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
        u.setPassword(pwenc.encode(pswd));
    }
}
