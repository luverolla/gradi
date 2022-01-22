package io.luverolla.gradi.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;

import io.luverolla.gradi.comparators.*;
import io.luverolla.gradi.entities.*;
import io.luverolla.gradi.entities.ResourcePermission.*;
import io.luverolla.gradi.exceptions.InvalidPropertyException;
import io.luverolla.gradi.filters.*;
import io.luverolla.gradi.repositories.*;
import io.luverolla.gradi.structures.EntityService;
import io.luverolla.gradi.structures.EntitySetRequest;
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
	private ResourcePermissionRepository permRepo;

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
			Map.entry("permissions", new ResourceFilterPermissionsUsers())
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

		return repo.save(found);
	}

	/**
	 * Gets all resources that a given user is authorized to read/write
	 *
	 * @param u given user
	 * @param min lowest permission needed to access resource
	 * @param req request object
	 *
	 * @return set of resources
	 */
	public SortedSet<Resource> get(User u, Type min, EntitySetRequest req)
	{
		SortedSet<Resource> found = get(req);

		found.removeIf(r -> r.getPermissions().stream().noneMatch(p ->
			p.getUser().equals(u) && p.getType().ordinal() >= min.ordinal()
		));

		return found;
	}

	/**
	 * Gets single resource by its code only if a given user is authorized to read
	 *
	 * @param u the given user
	 * @param min lowest permission needed to access resource
	 * @param code resource's code
	 *
	 * @throws NoSuchElementException if resource doesn't exist or user is not authorized to read it
	 * @return resource object, if it exists
	 */
	public Resource get(User u, Type min, String code)
	{
		Predicate<ResourcePermission> pr = p ->
			p.getUser().equals(u) && p.getType().ordinal() >= min.ordinal();

		Resource found = get(code);
		if(found.getPermissions().stream().noneMatch(pr))
			throw new NoSuchElementException();

		return found;
	}

	/**
	 * Updates resource only if a given user has write-permission to it
	 *
	 * @param u given user
	 * @param code resource's code
	 * @param data new resource data
	 *
	 * @throws NoSuchElementException if resource doesn't exist or user hasn't got write permissions on it
	 * @return updated resource, if no errors occur
	 */
	public Resource update(User u, String code, Resource data)
	{
		get(u, Type.WRITE, code); // needed to trigger exception if user hasn't got write permissions
		return update(code, data);
	}

	/**
	 * Get resource attribute if exists
	 *
	 * @param r given resource
	 * @param propName attribute's property's name
	 *
	 * @throws NoSuchElementException if attribute doesn't exist
	 * @return {@link ResourceAttribute} object, if exists
	 */
	public ResourceAttribute getAttribute(Resource r, String propName)
	{
		return r.getAttributes().stream()
			.filter(a -> a.getName().equals(r.getCode() + "#" + propName))
				.findFirst().orElseThrow(NoSuchElementException::new);
	}

	/**
	 * Adds attributes to resource
	 *
	 * @param r given resource
	 * @param data collection of {@link ResourceAttribute} objects
	 *
	 * @throws NoSuchElementException if given resource doesn't exist
	 * @return saved objects, with unique names
	 */
	public Resource addAttributes(Resource r, Collection<ResourceAttribute> data)
	{
		for(ResourceAttribute a : data)
		{
			a.setResource(r);
			a.setName(r + "#" + a.getName());
		}

		Set<ResourceAttribute> saved = new HashSet<>(data);
		r.setAttributes(saved);

		return repo.save(r);
	}

	/**
	 * Update single resource attribute
	 *
	 * Only attribute's value can be altered
	 *
	 * @param r given resource
	 * @param propName attribute's property's name
	 * @param data new attribute data
	 *
	 * @throws NoSuchElementException if given resource or attribute don't exist
	 * @return updated object, if no errors occurr
	 */
	public ResourceAttribute updateAttribute(Resource r, String propName, ResourceAttribute data)
	{
		// only attribute value can be altered
		ResourceAttribute found = getAttribute(r, propName);
		found.setValue(data.getValue());

		return attrRepo.save(found);
	}

	/**
	 * Deletes resource attribute
	 *
	 * @param r given resource
	 * @param propName attribute's property's name
	 *
	 * @throws NoSuchElementException if resource or attribute don't exist
	 */
	public void deleteAttribute(Resource r, String propName)
	{
		attrRepo.delete(getAttribute(r, propName));
	}

	/**
	 * Retrieves a {@link ResourceFile} object
	 *
	 * @param r given resource
	 * @param fileCode resource file's code
	 *
	 * @throws NoSuchElementException if resource or file don't exist
	 * @return {@link ResourceFile} object, if it exists
	 */
	public ResourceFile getFile(Resource r, String fileCode)
	{
		return r.getFiles().stream()
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
		File f = Paths.get(uploadDir + "/" + rf.getName()).toFile();
		if(!f.exists())
			throw new NoSuchElementException();

		return f;
	}

	/**
	 * Adds a file to a given resource and save it in filesystem
	 *
	 * Default upload directory is the one in <code>application.properties</code> file
	 *
	 * @param r given resource
	 * @param mpf file content
	 *
	 * @throws NoSuchElementException if resource doesn't exist
	 * @throws IOException if file saving gets into error
	 * @return saved {@link ResourceFile} object, if no errors occur
	 */
	public ResourceFile addFile(Resource r, MultipartFile mpf) throws IOException
	{
		if(mpf.getOriginalFilename() == null)
			throw new NullPointerException();

		String[] chnks = mpf.getOriginalFilename().split("\\.");
		String ext = chnks[chnks.length - 1];

		String code = EntityService.nextCode();
		ResourceFile file = new ResourceFile();
		file.setCode(code);
		file.setName(code + "." + ext);
		file.setResource(r);

		Path path = Paths.get(uploadDir + "/" + file.getName());
		Files.write(path, mpf.getBytes());

		return fileRepo.save(file);
	}

	/**
	 * Deletes file from resource
	 *
	 * @param r given resource
	 * @param fileCode given file's code
	 *
	 * @throws NoSuchElementException if resource or file don't exist
	 * @throws IOException if file deletion goes into error
	 */
	public void deleteFile(Resource r, String fileCode) throws IOException
	{
		ResourceFile found = getFile(r, fileCode);

		File file = Paths.get(uploadDir + "/" + found.getName()).toFile();
		if(!file.delete()) throw new IOException();

		fileRepo.delete(found);
	}

	/**
	 * Sets permissions to resource
	 *
	 * Deletes all previously stored permissions and readd them from zero
	 *
	 * @param r given resource
	 * @param data permission set
	 *
	 * @throws NoSuchElementException if resource doesn't exist
	 * @return updated resource, if it exists
	 */
	public Set<ResourcePermission> setPermissions(Resource r, Set<ResourcePermission> data)
	{
		// permission of type 'FULL' must always be preserved
		ResourcePermission full = r.getPermissions()
			.stream().filter(p -> p.getType().equals(Type.FULL))
				.findFirst().orElseThrow(NoSuchElementException::new);

		permRepo.resetResource(r);
		permRepo.save(full);

		for(ResourcePermission p : data)
			p.setResource(r);

		return new HashSet<>((List<ResourcePermission>) permRepo.saveAll(data));
	}
}
