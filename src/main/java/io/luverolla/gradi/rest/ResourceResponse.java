package io.luverolla.gradi.rest;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import io.luverolla.gradi.entities.Resource;
import io.luverolla.gradi.structures.EntityResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ResourceResponse extends EntityResponse<Resource>
{
	@AllArgsConstructor
	private static class Permission
	{
		private String userURI;
		private String role;
	}
	
	@AllArgsConstructor
	private static class File
	{
		private String name;
		private String URI;
	}
	
	@AllArgsConstructor
	private static class Attribute
	{
		private String name;
		private String type;
		private String value;
	}
	
	private String code;
	private OffsetDateTime createdAt;
	private OffsetDateTime updatedAt;
    private String name;
    private String description;
    private String typeURI;
    private String parentURI;
    private Set<String> childrenURIs;   
    private Set<File> files;
    private Set<Attribute> attributes;
    private Set<Permission> permissions;
    
	public ResourceResponse(Resource r)
	{
		super(r);
	}
	
	@Override
	public ResourceResponse build(Resource r)
	{
		ResourceResponse res = new ResourceResponse(r);
		
		res.setCode(r.getCode());
		res.setCreatedAt(r.getCreatedAt());
		res.setUpdatedAt(r.getUpdatedAt());
		res.setName(name);
		res.setDescription(description);
		res.setTypeURI(r.getType().getURI());
		res.setParentURI(r.getParent().getURI());
		
		res.setChildrenURIs(r.getChildren()
			.stream().map(Resource::getURI)
				.collect(Collectors.toSet()));
		
		res.setFiles(r.getFiles()
			.stream().map(e -> new File(e.getName(), e.getURI()))
				.collect(Collectors.toSet()));
		
		res.setAttributes(r.getAttributes()
			.stream().map(e -> new Attribute(e.getProperty().getName(), e.getProperty().getType().toString(), e.getValue()))
				.collect(Collectors.toSet()));
		
		res.setPermissions(r.getPermissions()
			.stream().map(e -> new Permission(e.getUser().getURI(), e.getType().toString()))
				.collect(Collectors.toSet()));
		
		return res;
	}
}
