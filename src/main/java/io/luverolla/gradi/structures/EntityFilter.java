package io.luverolla.gradi.structures;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.aspectj.apache.bcel.classfile.Code;

/**
 * Generic entity filter
 *
 * Can take in account a value to help filtering.
 * It can be a single object, or a collection.
 *
 * For example, when filtering by a numeric property,
 * the value can be a list of two element:
 * an upper and a lower bound, where tested value must be contained into
 *
 * @param <E> entity type
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class EntityFilter<E, V>
{
	private V value;

	public void setValue(Object value)
	{
		try {
			this.value = (V) value;
		}
		catch (ClassCastException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Applies filter to a given entity
	 * @param entity the given entity
	 * @return `true` if entity matches filter's conditions, `false` otherwise
	 */
	public abstract boolean test(E entity);
}