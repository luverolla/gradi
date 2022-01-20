package io.luverolla.gradi.filters;

import io.luverolla.gradi.entities.User;

public class UserFilterSurname extends Filter<User, String>
{
	@Override
	public boolean test(User u)
	{
		return u.getSurname().trim().toLowerCase().contains(getValue().trim().toLowerCase());
	}
}
