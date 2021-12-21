package io.luverolla.gradi.filters;

import java.util.Set;

import io.luverolla.gradi.entities.User;
import io.luverolla.gradi.structures.EntityFilter;

public class UserFilterRole extends EntityFilter<User, Set<User.Role>>
{
	@Override
	public boolean test(User u)
	{
		return getValue().contains(u.getRole());
	}
}
