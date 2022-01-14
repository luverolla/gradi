package io.luverolla.gradi.comparators;

import io.luverolla.gradi.entities.Message;

import java.util.Comparator;

public class MessageComparatorVisibility implements Comparator<Message>
{
    @Override
    public int compare(Message o1, Message o2)
    {
        return o1.getVisibility().compareTo(o2.getVisibility());
    }
}
