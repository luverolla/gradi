package io.luverolla.gradi.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import io.luverolla.gradi.comparators.*;
import io.luverolla.gradi.entities.ResourceAttribute;
import io.luverolla.gradi.entities.ResourceFile;
import io.luverolla.gradi.entities.ResourceProperty;
import io.luverolla.gradi.exceptions.InvalidPropertyException;
import io.luverolla.gradi.exceptions.ResourceTypeMismatchException;
import io.luverolla.gradi.filters.*;
import io.luverolla.gradi.repositories.ResourceAttributeRepository;
import io.luverolla.gradi.repositories.ResourceFileRepository;
import io.luverolla.gradi.repositories.ResourcePropertyRepository;
import io.luverolla.gradi.entities.Resource;
import io.luverolla.gradi.repositories.ResourceRepository;
import io.luverolla.gradi.structures.EntityService;
import io.luverolla.gradi.structures.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ResourceService extends EntityService<Resource>
{
	@Value("${gradi.system.directories.upload}")
	private String uploadDir;

	@Autowired
	private ResourceRepository repo;

	@Autowired
	private ResourcePropertyRepository propRepo;

	@Autowired
	private ResourceAttributeRepository attrRepo;

	@Autowired
	private ResourceFileRepository fileRepo;

	@Autowired
	public PagingAndSortingRepository<Resource, String> repo()
	{
		return repo;
	}

	@Override
	protected Map<String, Comparator<Resource>> getComparatorMap()
	{
		return Map.ofEntries(
			Map.entry("code", new EntityComparatorCode<>()),
			Map.entry("name", new ResourceComparatorName()),
			Map.entry("createdAt", new EntityComparatorCreatedAt<>()),
			Map.entry("updatedAt", new EntityComparatorUpdatedAt<>()),
			Map.entry("type", new ResourceComparatorType()),
			Map.entry("permissions", new ResourceComparatorPermissionsSize()),
			Map.entry("visibility", new ResourceComparatorVisibility())
		);
	}

	@Override
	protected Map<String, Filter<Resource, ?>> getFilterMap()
	{
		return Map.ofEntries(
			Map.entry("code", new EntityFilterCode<>()),
			Map.entry("name", new ResourceFilterName()),
			Map.entry("createdAt", new EntityFilterCreatedAt<>()),
			Map.entry("updatedAt", new EntityFilterUpdatedAt<>()),
			Map.entry("type", new ResourceFilterType()),
			Map.entry("permissions", new ResourceFilterPermissions())
		);
	}

	/**
	 * Get right filter, taking in account custom properties' names
	 * @param m filter rule
	 * @return right Resource filter
	 */
	@Override
	protected Filter<Resource, ?> getFilter(Map.Entry<String, ?> m)
	{
		// check if map has any filter for property
		if(getFilterMap().containsKey(m.getKey()))
			return super.getFilter(m);

		// check if is custom property
		Optional<ResourceProperty> prop = propRepo.findByName(m.getKey());
		return prop.isEmpty() ? null : new ResourceFilterCustom(prop.get(), m.getValue());
	}

	/**
	 * Get right filter, taking in account custom properties' names
	 * @param m filter rule
	 * @return right Resource filter
	 */
	@Override
	protected Comparator<Resource> getComparator(Map.Entry<String, String> m)
	{
		// check if map has any filter for property
		if(getComparatorMap().containsKey(m.getKey()))
			return super.getComparator(m);

		// check if is custom property
		Optional<ResourceProperty> prop = propRepo.findByName(m.getKey());
		if(prop.isEmpty())
			throw new InvalidPropertyException();

		Comparator<Resource> comp = new ResourceComparatorCustom<>(prop.get());
		return m.getValue().equals("desc") ? comp.reversed() : comp;
	}

	@Override
	public Resource update(String code, Resource data)
	{
		Optional<Resource> tg = repo.findById(code);
		if(tg.isEmpty())
			throw new NoSuchElementException();

		Resource found = tg.get();
		found.setName(data.getName());
		found.setVisibility(data.getVisibility());
		found.setDescription(data.getDescription());
		found.setParent(data.getParent());
		found.setType(data.getType());

		return found;
	}

	/**
	 * Get resource attribute if exists
	 *
	 * @param resCode resource's code
	 * @param propName attribute's property's name
	 *
	 * @throws NoSuchElementException if attribute doesn't exist
	 * @return {@link ResourceAttribute} object, if exists
	 */
	public ResourceAttribute getAttribute(String resCode, String propName)
	{
		return get(resCode).getAttributes()
			.stream().filter(a -> a.getName().equals(resCode + "#" + propName))
				.findFirst().orElseThrow(NoSuchElementException::new);
	}

	/**
	 * Adds attributes to resource
	 *
	 * @param resCode given resource's code
	 * @param data collection of {@link ResourceAttribute} objects
	 *
	 * @throws NoSuchElementException if given resource doesn't exist
	 * @return saved objects, with unique names
	 */
	public Set<ResourceAttribute> addAttributes(String resCode, Collection<ResourceAttribute> data)
	{
		Resource found = get(resCode);

		for(ResourceAttribute a : data)
		{
			if(!a.belongsTo(found))
				throw new ResourceTypeMismatchException();

			a.setResource(found);
			a.setName(resCode + "#" + a.getProperty().getName());
		}

		return (Set<ResourceAttribute>) attrRepo.saveAll(data);
	}

	/**
	 * Update single resource attribute
	 *
	 * Only attribute's value can be altered
	 *
	 * @param resCode given resource's code
	 * @param propName attribute's property's name
	 * @param data new attribute data
	 *
	 * @throws NoSuchElementException if given resource or attribute don't exist
	 * @return updated object, if no errors occurr
	 */
	public ResourceAttribute updateAttribute(String resCode, String propName, ResourceAttribute data)
	{
		// only attribute value can be altered
		ResourceAttribute found = getAttribute(resCode, propName);
		found.setValue(data.getValue());

		return attrRepo.save(found);
	}

	/**
	 * Deletes resource attribute
	 *
	 * @param resCode given resource's code
	 * @param propName attribute's property's name
	 *
	 * @throws NoSuchElementException if resource or attribute don't exist
	 */
	public void deleteAttribute(String resCode, String propName)
	{
		attrRepo.delete(getAttribute(resCode, propName));
	}

	/**
	 * Retrieves a {@link ResourceFile} object
	 *
	 * @param resCode given resource's code
	 * @param fileCode resource file's code
	 *
	 * @throws NoSuchElementException if resource or file don't exist
	 * @return {@link ResourceFile} object, if it exists
	 */
	public ResourceFile getFile(String resCode, String fileCode)
	{
		return get(resCode).getFiles().stream()
			.filter(f -> f.getCode().equalsIgnoreCase(fileCode))
				.findFirst().orElseThrow(NoSuchElementException::new);
	}

	/**
	 * Get file content by {@link ResourceFile} object
	 *
	 * @param rf given object
	 *
	 * @throws NoSuchElementException if resource file object doesn't exist
	 * @return file content, as a {@link File} object
	 */
	public File getFileObject(ResourceFile rf)
	{
		File f = Paths.get(uploadDir + "/" + rf.getCode()).toFile();
		if(!f.exists())
			throw new NoSuchElementException();

		return f;
	}

	/**
	 * Adds a file to a given resource and save it in filesystem
	 *
	 * Default upload directory is the one in <code>application.properties</code> file
	 *
	 * @param resCode given resource's code
	 * @param mpf file content
	 *
	 * @throws NoSuchElementException if resource doesn't exist
	 * @throws IOException if file saving gets into error
	 * @return saved {@link ResourceFile} object, if no errors occur
	 */
	public ResourceFile addFile(String resCode, MultipartFile mpf) throws IOException
	{
		if(mpf.getOriginalFilename() == null)
			throw new NullPointerException();

		String[] chnks = mpf.getOriginalFilename().split("\\.");
		String ext = chnks[chnks.length - 1];

		String code = EntityService.nextCode();
		ResourceFile file = new ResourceFile();
		file.setCode(code);
		file.setName(code + "." + ext);
		file.setResource(get(resCode));

		Path path = Paths.get(uploadDir + file.getName());
		Files.write(path, mpf.getBytes());

		return fileRepo.save(file);
	}

	/**
	 * Deletes file from resource
	 *
	 * @param resCode given resource's code
	 * @param fileCode given file's code
	 *
	 * @throws NoSuchElementException if resource or file don't exist
	 * @throws IOException if file deletion goes into error
	 */
	public void deleteFile(String resCode, String fileCode) throws IOException
	{
		ResourceFile found = getFile(resCode, fileCode);

		File file = Paths.get(uploadDir + "/" + found.getName()).toFile();
		if(!file.delete()) throw new IOException();

		fileRepo.delete(found);
	}
}
