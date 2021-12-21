package io.luverolla.gradi.services;

import java.util.Map;

import io.luverolla.gradi.structures.EntityComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import io.luverolla.gradi.entities.Resource;
import io.luverolla.gradi.repositories.ResourceRepository;
import io.luverolla.gradi.structures.EntityService;
import io.luverolla.gradi.structures.EntityFilter;

public class ResourceService extends EntityService<Resource>
{
	@Autowired
	private ResourceRepository repo;
	
	@Override
	protected Map<String, EntityComparator<Resource>> getComparatorMap()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Map<String, EntityFilter<Resource, ?>> getFilterMap()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected JpaRepository<Resource, Long> repo()
	{
		return repo;
	}
}
