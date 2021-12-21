package io.luverolla.gradi.structures;

import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Entity's REST model
 * 
 * This defines how the JSON REST response for the desidered entity should look
 * For entities in relation, only their URI is shown, the client will then make another REST call to get data.
 *
 * @param <E> entity type, derived from {@link BaseEntity}
 */
@NoArgsConstructor
@AllArgsConstructor
public abstract class EntityResponse<E extends BaseEntity>
{
	/**
	 * Converts original entity's comparator to entity REST response model type
	 * This is needed to preserve original order when collecting stream elements into a Set
	 *
	 * @see Collectors#toSet()
	 *
	 * @param <E> entity type, derived from {@link BaseEntity}
	 */
	@AllArgsConstructor
	private static class MappedComparator<E extends BaseEntity> implements Comparator<EntityResponse<E>>
	{
		private final Comparator<? super E> comp;
		
		@Override
		public int compare(EntityResponse<E> e1, EntityResponse<E> e2)
		{
			return comp.compare(e1.getE(), e2.getE());
		}
	}
	
	@JsonIgnore
	private E e;
	
	protected E getE()
	{
		return e;
	}
	
	protected void setE(E e)
	{
		this.e = e;
	}
	
	/**
	 * Builds model for single entity. Implementation depends on entity's type
	 * 
	 * @param entity object to be converted to REST response
	 * @return rest response model
	 */
	public abstract EntityResponse<E> build(@NotNull E entity);
	
	/**
	 * Builds model for set of an entity set. Relies on abstract {@link EntityResponse#build(BaseEntity)} method
	 * 
	 * @param src the set of entities to convert
	 * @return set of entity REST models
	 */
	public Set<EntityResponse<E>> build(@NotNull SortedSet<E> src)
	{
		MappedComparator<E> cmp = new MappedComparator<E>(src.comparator());
		
		return src.stream().map(this::build)
			.collect(Collectors.toCollection( () -> new TreeSet<>(cmp) ));
	}
}