package io.luverolla.gradi.structures;

public interface Filter<E extends BaseEntity>
{
	boolean test(E entity);
}
