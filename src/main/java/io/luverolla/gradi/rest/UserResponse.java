package io.luverolla.gradi.rest;

import java.time.OffsetDateTime;

import java.util.Set;
import java.util.stream.Collectors;

import io.luverolla.gradi.entities.User;
import io.luverolla.gradi.structures.EntityResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse extends EntityResponse<User>
{
	@AllArgsConstructor
	private static class Permission
	{
		private String role;
		private String resourceURI;
	}
	
	private String code;
	private OffsetDateTime createdAt;
	private OffsetDateTime updatedAt;
	private String name;
	private String surname;
	private String description;
	private String email;
	private String role;
	private Set<Permission> permissions;
	
	public UserResponse(User u)
	{
		super(u);
	}
	
	@Override
	public UserResponse build(User user)
	{
		UserResponse res = new UserResponse(user);
		
		res.setCode(user.getCode());
		res.setCreatedAt(user.getCreatedAt());
		res.setUpdatedAt(user.getUpdatedAt());
		res.setName(user.getName());
		res.setSurname(user.getSurname());
		res.setDescription(user.getDescription());
		res.setEmail(user.getEmail());
		res.setRole(user.getRole().toString());
		
		res.setPermissions(user.getPermissions()
			.stream().map(e -> new Permission(e.getType().toString(), e.getResource().getURI()))
				.collect(Collectors.toSet()));
		
		return res;
	}
}
