package io.luverolla.gradi.filters;

import io.luverolla.gradi.entities.User;

public class UserFilterEmail extends Filter<User, String>
{
	@Override
	public boolean test(User u)
	{
		return u.getEmail().trim().toLowerCase().contains(getValue().trim().toLowerCase());
	}
}
