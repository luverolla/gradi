package io.luverolla.gradi.filters;

import io.luverolla.gradi.structures.CodedEntity;
import io.luverolla.gradi.structures.EntityFilter;

public class EntityFilterCode<E extends CodedEntity> extends EntityFilter<E, String>
{
	@Override
	public boolean test(E e)
	{
		return e.getCode().equalsIgnoreCase(getValue());
	}
}
