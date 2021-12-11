package io.luverolla.gradi.filters;

import java.util.List;

import io.luverolla.gradi.entities.User;
import io.luverolla.gradi.structures.Filter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserFilterRole implements Filter<User>
{
	private Object value;
	
	@Override
	public boolean test(User u)
	{
		return ((List<String>) value).contains(u.getRole().toString());
	}
}
