package io.luverolla.gradi.services;

import io.luverolla.gradi.comparators.EntityComparatorCode;
import io.luverolla.gradi.comparators.EntityComparatorCreatedAt;
import io.luverolla.gradi.comparators.EntityComparatorUpdatedAt;
import io.luverolla.gradi.comparators.MessageComparatorSubject;
import io.luverolla.gradi.comparators.MessageComparatorVisibility;
import io.luverolla.gradi.comparators.MessageComparatorType;
import io.luverolla.gradi.entities.Message;
import io.luverolla.gradi.entities.User;
import io.luverolla.gradi.filters.EntityFilterCode;
import io.luverolla.gradi.filters.EntityFilterCreatedAt;
import io.luverolla.gradi.filters.EntityFilterUpdatedAt;
import io.luverolla.gradi.filters.MessageFilterSubject;
import io.luverolla.gradi.filters.MessageFilterVisibility;
import io.luverolla.gradi.filters.MessageFilterType;
import io.luverolla.gradi.filters.MessageFilterText;
import io.luverolla.gradi.repositories.MessageRepository;
import io.luverolla.gradi.repositories.UserRepository;
import io.luverolla.gradi.structures.EntityService;
import io.luverolla.gradi.structures.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MessageService extends EntityService<Message>
{
    @Autowired
    private MessageRepository repo;

    @Autowired
    private MailingService mailingService;

    @Autowired
    private UserRepository userRepo;

    @Override
    public PagingAndSortingRepository<Message, String> repo()
    {
        return repo;
    }

    @Override
    protected Map<String, Comparator<Message>> getComparatorMap()
    {
        return Map.ofEntries(
            Map.entry("code", new EntityComparatorCode<>()),
            Map.entry("subject", new MessageComparatorSubject()),
            Map.entry("visibility", new MessageComparatorVisibility()),
            Map.entry("type", new MessageComparatorType()),
            Map.entry("createdAt", new EntityComparatorCreatedAt<>()),
            Map.entry("updatedAt", new EntityComparatorUpdatedAt<>())
        );
    }
    
    @Override
    protected Map<String, Filter<Message, ?>> getFilterMap()
    {
    	return Map.ofEntries(
            Map.entry("code", new EntityFilterCode<>()),
            Map.entry("subject", new MessageFilterSubject()),
            Map.entry("visibility", new MessageFilterVisibility()),
            Map.entry("type", new MessageFilterType()),
            Map.entry("text", new MessageFilterText()),
            Map.entry("createdAt", new EntityFilterCreatedAt<>()),
            Map.entry("updatedAt", new EntityFilterUpdatedAt<>())
        );
    }

    /**
     * Adds given messages and notify recipients via email
     * @param data data to add
     *
     * @return saved messages, if no errors occur
     */
    @Override
    public Set<Message> add(Collection<Message> data)
    {
        Set<Message> saved = super.add(data);
        for(Message m : saved)
            mailingService.notifyMessage(m, Objects.requireNonNull(getRecipients(m)));

        return saved;
    }

    @Override
    public Message update(String code, Message data)
    {
        Optional<Message> tg = repo.findById(code);
        if(tg.isEmpty())
            throw new NoSuchElementException();

        Message found = tg.get();

        found.setSubject(data.getSubject());
        found.setVisibility(data.getVisibility());
        found.setType(data.getType());
        found.setText(data.getText());

        return repo.save(found);
    }

    /**
     * Gets message recipients according to visibility
     *
     * @param m given message
     *
     * @return set of recipients
     */
    private Set<User> getRecipients(Message m)
    {
        switch (m.getVisibility())
        {
            case GLOBAL:
                return new HashSet<>((List<User>) userRepo.findAll());

            case EDITORS:
                return new HashSet<>((List<User>) userRepo.findAllEditors());

            case PRIVATE:
                return m.getRecipients();
        }

        return Set.of();
    }
}