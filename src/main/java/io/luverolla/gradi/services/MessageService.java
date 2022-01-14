package io.luverolla.gradi.services;

import io.luverolla.gradi.comparators.EntityComparatorCode;
import io.luverolla.gradi.comparators.EntityComparatorCreatedAt;
import io.luverolla.gradi.comparators.EntityComparatorUpdatedAt;
import io.luverolla.gradi.comparators.MessageComparatorSubject;
import io.luverolla.gradi.comparators.MessageComparatorVisibility;
import io.luverolla.gradi.comparators.MessageComparatorType;
import io.luverolla.gradi.entities.Message;
import io.luverolla.gradi.filters.EntityFilterCode;
import io.luverolla.gradi.filters.EntityFilterCreatedAt;
import io.luverolla.gradi.filters.EntityFilterUpdatedAt;
import io.luverolla.gradi.filters.MessageFilterSubject;
import io.luverolla.gradi.filters.MessageFilterVisibility;
import io.luverolla.gradi.filters.MessageFilterType;
import io.luverolla.gradi.filters.MessageFilterText;
import io.luverolla.gradi.repositories.MessageRepository;
import io.luverolla.gradi.rest.EntitySetRequest;
import io.luverolla.gradi.structures.CodedEntity;
import io.luverolla.gradi.structures.EntityService;
import io.luverolla.gradi.structures.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageService extends EntityService<Message>
{
    @Autowired
    private MessageRepository repo;

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

    public SortedSet<Message> getMessages(EntitySetRequest req)
    {
        Integer page = req.getPage(), lim = req.getLimit();
        boolean paging = page != null && lim != null;

        Collection<Message> data = paging ? repo.findAll(page, lim) : repo.findAll();

        return data.stream().filter(r -> chainFilters(req).test(r))
            .collect(Collectors.toCollection(() -> new TreeSet<>(chainComparators(req))));
    }

    public Message getOneMessage(String code)
    {
        Optional<Message> Message = repo.findById(code);
        return Message.orElseThrow(NoSuchElementException::new);
    }

    public Set<Message> add(Collection<Message> src)
    {
        for(Message u : src)
            u.setCode(CodedEntity.nextCode());

        return new HashSet<>(repo.saveAll(src));
    }

    public Message updateOneMessage(String code, Message data)
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
    
    public void deleteOneMessage(String code)
    {
        repo.delete(getOneMessage(code));
    }
}