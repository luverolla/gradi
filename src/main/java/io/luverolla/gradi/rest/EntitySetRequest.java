package io.luverolla.gradi.rest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    private Optional<Integer> page;
    private Optional<Integer> limit;
    private Optional<List<String>> orders;
    // key is entity's property, value is searched value
    private Optional<Map<String, Object>> filters;
}
