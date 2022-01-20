package io.luverolla.gradi.filters;

import io.luverolla.gradi.entities.CodedEntity;
import io.luverolla.gradi.structures.Filter;

public class EntityFilterCode<E extends CodedEntity> extends Filter<E, String>
{
	@Override
	public boolean test(E e)
	{
		return e.getCode().trim().equalsIgnoreCase(getValue().trim());
	}
}
