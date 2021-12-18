package io.luverolla.gradi.services;

import java.util.Comparator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import io.luverolla.gradi.entities.Resource;
import io.luverolla.gradi.repositories.ResourceRepository;
import io.luverolla.gradi.structures.EntityService;
import io.luverolla.gradi.structures.Filter;

public class ResourceService extends EntityService<Resource>
{
	@Autowired
	private ResourceRepository repo;
	
	@Override
	protected Map<String, Class<? extends Comparator<Resource>>> getComparatorMap()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Map<String, Class<? extends Filter<Resource>>> getFilterMap()
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
