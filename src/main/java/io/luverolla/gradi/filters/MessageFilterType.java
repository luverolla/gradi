package io.luverolla.gradi.filters;

import io.luverolla.gradi.entities.Message;

import java.util.Set;

public class MessageFilterType extends Filter<Message, Set<Message.Type>>
{
    @Override
    public boolean test(Message entity)
    {
        return getValue().contains(entity.getType());
    }
}
