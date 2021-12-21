package io.luverolla.gradi.filters;

import io.luverolla.gradi.entities.User;
import io.luverolla.gradi.structures.EntityFilter;

public class UserFilterEmail extends EntityFilter<User, String>
{
	@Override
	public boolean test(User u)
	{
		return u.getEmail().toLowerCase().contains(getValue().toLowerCase());
	}
}
