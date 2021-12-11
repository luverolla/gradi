package io.luverolla.gradi.filters;

import io.luverolla.gradi.entities.User;
import io.luverolla.gradi.structures.Filter;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserFilterCode implements Filter<User>
{
	private Object value;
	
	@Override
	public boolean test(User u)
	{
		return u.getCode().equalsIgnoreCase((String) value);
	}
}
