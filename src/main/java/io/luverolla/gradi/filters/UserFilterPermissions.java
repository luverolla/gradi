package io.luverolla.gradi.filters;

import java.util.List;

import io.luverolla.gradi.entities.User;
import io.luverolla.gradi.structures.Filter;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserFilterPermissions implements Filter<User>
{
	private Object value;
	
	@Override
	public boolean test(User u)
	{
		Integer min = ((List<Integer>) value).get(0);
		Integer max = ((List<Integer>) value).get(1);
		
		boolean res = true;
		
		if(min != null)
			res = res && u.getPermissions().size() > min;
			
		if(max != null)
			res = res && u.getPermissions().size() < max;
		
		return res;
	}
}
