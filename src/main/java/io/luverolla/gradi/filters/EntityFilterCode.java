package io.luverolla.gradi.filters;

import io.luverolla.gradi.structures.CodedEntity;
import io.luverolla.gradi.structures.Filter;

public class EntityFilterCode<E extends CodedEntity> extends Filter<E, Integer>
{
	@Override
	public boolean test(E e)
	{
		return e.getCode().equals(getValue());
	}
}
