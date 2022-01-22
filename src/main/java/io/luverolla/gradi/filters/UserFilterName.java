package io.luverolla.gradi.filters;

import io.luverolla.gradi.entities.User;
import io.luverolla.gradi.structures.Filter;

public class UserFilterName extends Filter<User, String>
{
	@Override
	public boolean test(User u)
	{
		return u.getName().trim().toLowerCase().contains(getValue().trim().toLowerCase());
	}
}
