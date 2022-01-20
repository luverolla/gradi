package io.luverolla.gradi.filters;

import io.luverolla.gradi.entities.Message;

import org.jsoup.Jsoup;

public class MessageFilterText extends Filter<Message, String>
{
    @Override
    public boolean test(Message entity)
    {
        return Jsoup.parse(entity.getText()).text().trim().toLowerCase()
            .contains(getValue().trim().toLowerCase());
    }
}