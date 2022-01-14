package io.luverolla.gradi.filters;

import io.luverolla.gradi.entities.Message;
import io.luverolla.gradi.structures.Filter;

import java.util.Set;

public class MessageFilterVisibility extends Filter<Message, Set<Message.Visibility>>
{
    @Override
    public boolean test(Message entity)
    {
        return getValue().contains(entity.getVisibility());
    }
}
