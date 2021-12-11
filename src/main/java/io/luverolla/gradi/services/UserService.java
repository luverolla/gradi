package io.luverolla.gradi.services;

import java.util.Comparator;
import java.util.Map;
import static java.util.Map.entry;

import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.luverolla.gradi.entities.User;
import io.luverolla.gradi.repositories.UserRepository;
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
    public Comparator<User> getComparator(String s)
    {
        // parameter order is in form '{PROP},{ASC|DESC}'
        String prop = s.split(",")[0].toLowerCase();
        String way = s.split(",")[1].toLowerCase();

        // sanity check
        if(!CMPTS.containsKey(prop) || !(way == "asc" || way == "desc"))
            return null;

        try
        {
            // our comparator's constructors take a 'desc' boolean varible as argument, if order is descendent
            return (Comparator<User>) CMPTS.get(prop).getDeclaredConstructor(Boolean.class).newInstance(way == "desc");
        }
        catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
        {
            e.printStackTrace();
        }

        return null;
    }
    
    @Override
    public Filter<User> getFilter(Map<String, Object> m)
    {
    	// there is only one entry
    	String key = m.keySet().iterator().next();
    	Object value = m.get(key);
    	
        // sanity check
        if(!FLTRS.containsKey(key))
            return null;

        try
        {
            // our filter's constructors take a 'value' object
            return (Filter<User>) CMPTS.get(key).getDeclaredConstructor(Object.class).newInstance(value);
        }
        catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
