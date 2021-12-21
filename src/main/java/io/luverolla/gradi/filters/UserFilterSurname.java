package io.luverolla.gradi.filters;

import io.luverolla.gradi.entities.User;
import io.luverolla.gradi.structures.EntityFilter;

public class UserFilterSurname extends EntityFilter<User, String>
{
	@Override
	public boolean test(User u)
	{
		return u.getSurname().toLowerCase().contains(getValue().toLowerCase());
	}
}
