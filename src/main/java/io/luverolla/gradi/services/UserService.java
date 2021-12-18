package io.luverolla.gradi.services;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import static java.util.Map.entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import io.luverolla.gradi.entities.User;
import io.luverolla.gradi.repositories.UserRepository;
import io.luverolla.gradi.rest.EntitySetRequest;
import io.luverolla.gradi.structures.EntityService;
import io.luverolla.gradi.structures.Filter;

@Service
public class UserService extends EntityService<User>
{
    @Autowired
    private UserRepository repo;

	private static final Map<String, Class<? extends Comparator<User>>> CMPTS = Map.ofEntries(
        entry("code", io.luverolla.gradi.comparators.UserComparatorCode.class),
        entry("name", io.luverolla.gradi.comparators.UserComparatorName.class),
        entry("surname", io.luverolla.gradi.comparators.UserComparatorSurname.class),
        entry("email", io.luverolla.gradi.comparators.UserComparatorEmail.class),
        entry("permissions", io.luverolla.gradi.comparators.UserComparatorPermissions.class),
        entry("role", io.luverolla.gradi.comparators.UserComparatorRole.class)
    );
    
    private static final Map<String, Class<? extends Filter<User>>> FLTRS = Map.ofEntries(
        entry("code", io.luverolla.gradi.filters.UserFilterCode.class),
        entry("name", io.luverolla.gradi.filters.UserFilterName.class),
        entry("surname", io.luverolla.gradi.filters.UserFilterSurname.class),
        entry("email", io.luverolla.gradi.filters.UserFilterEmail.class),
        entry("permissions", io.luverolla.gradi.filters.UserFilterPermissions.class),
        entry("role", io.luverolla.gradi.filters.UserFilterRole.class)
    );

    @Override
    protected Map<String, Class<? extends Comparator<User>>> getComparatorMap()
    {
    	return CMPTS;
    }
    
    @Override
    protected Map<String, Class<? extends Filter<User>>> getFilterMap()
    {
    	return FLTRS;
    }
    
    @Override
    protected JpaRepository<User, Long> repo()
    {
    	return repo;
    }
    
    @Override
    public Set<User> get(EntitySetRequest<User> req)
    {
    	Set<User> data = super.get(req);
    	
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
}
