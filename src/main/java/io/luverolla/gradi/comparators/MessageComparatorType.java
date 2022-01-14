package io.luverolla.gradi.comparators;

import io.luverolla.gradi.entities.Message;

import java.util.Comparator;

public class MessageComparatorType implements Comparator<Message>
{
    @Override
    public int compare(Message o1, Message o2)
    {
        return o1.getType().compareTo(o2.getType());
    }
}
