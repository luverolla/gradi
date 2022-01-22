package io.luverolla.gradi.filters;

import io.luverolla.gradi.entities.Message;
import io.luverolla.gradi.structures.Filter;

public class MessageFilterSubject extends Filter<Message, String>
{
    @Override
    public boolean test(Message entity)
    {
        return entity.getSubject().trim().toLowerCase().contains(getValue().trim().toLowerCase());
    }
}
