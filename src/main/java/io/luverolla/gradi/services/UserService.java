package io.luverolla.gradi.services;

import java.util.Comparator;
import java.util.Map;
import static java.util.Map.entry;

import java.lang.reflect.InvocationTargetException;

import org.springframework.stereotype.Service;

import io.luverolla.gradi.entities.User;
import io.luverolla.gradi.rest.EntitySetRequest;

@Service
public class UserService
{
    private static final Map<String, Class<? extends Comparator<User>>> CMPTS = Map.ofEntries(
        entry("code", io.luverolla.gradi.comparators.UserComparatorCode.class),
        entry("name", io.luverolla.gradi.comparators.UserComparatorName.class),
        entry("surname", io.luverolla.gradi.comparators.UserComparatorSurname.class),
        entry("email", io.luverolla.gradi.comparators.UserComparatorEmail.class),
        entry("permissions", io.luverolla.gradi.comparators.UserComparatorPermissions.class),
        entry("role", io.luverolla.gradi.comparators.UserComparatorRole.class)
    );

    private Comparator<User> getComparator(EntitySetRequest<User> req)
    {
        // if no order is specified, then no comparator is needed
        if(req.getOrder().isEmpty())
            return null;

        // parameter order is in form '{PROP},{ASC|DESC}'
        String order = req.getOrder().get();
        String prop = order.split(",")[0].toLowerCase();
        String way = order.split(",")[1].toLowerCase();

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
}
