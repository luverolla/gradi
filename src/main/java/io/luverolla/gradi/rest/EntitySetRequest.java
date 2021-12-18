package io.luverolla.gradi.rest;

import java.util.List;
import java.util.Map;

import io.luverolla.gradi.structures.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EntitySetRequest<E extends BaseEntity>
{
    private Integer page;
    private Integer limit;
    private List<String> orders;
    // key is entity's property, value is searched value
    private Map<String, Object> filters;
}
