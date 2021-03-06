package io.luverolla.gradi.structures;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * REST request model to retrieve a set of entities with given sorting and filters
 *
 * In field {@link EntitySetRequest#orders} the value string is <code>asc</code> or <code>desc</code>
 * In field {@link EntitySetRequest#filters} the value object's actual type depends on filter
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EntitySetRequest
{
    private Integer page;
    private Integer limit;
    private Map<String, String> orders;
    private Map<String, Object> filters;
}
