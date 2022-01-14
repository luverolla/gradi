package io.luverolla.gradi.comparators;

import io.luverolla.gradi.entities.Message;

import java.util.Comparator;

public class MessageComparatorSubject implements Comparator<Message>
{
    @Override
    public int compare(Message o1, Message o2)
    {
        return o1.getSubject().trim().compareToIgnoreCase(o2.getSubject().trim());
    }
}
