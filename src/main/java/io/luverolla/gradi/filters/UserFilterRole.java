package io.luverolla.gradi.filters;

import java.util.Set;

import io.luverolla.gradi.entities.User;

public class UserFilterRole extends Filter<User, Set<User.Role>>
{
	@Override
	public boolean test(User u)
	{
		return getValue().contains(u.getRole());
	}
}
