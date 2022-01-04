package io.luverolla.gradi.controllers;

import io.luverolla.gradi.entities.Resource;
import io.luverolla.gradi.rest.ResourceResponse;
import io.luverolla.gradi.rest.UserResponse;
import io.luverolla.gradi.services.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.luverolla.gradi.entities.User;
import io.luverolla.gradi.rest.EntitySetRequest;
import io.luverolla.gradi.services.UserService;

import java.util.Set;
import java.util.SortedSet;

@RestController
public class AdminController
{
	@Autowired
	private UserService ser;

	@Autowired
	private ResourceService resSer;
	
	@GetMapping("/api/admin/users")
	public ResponseEntity<?> getAllUsers(@RequestBody EntitySetRequest<User> req)
	{
		return ResponseEntity.ok(
			new UserResponse().build(ser.get(req))
		);
	}

	@PostMapping("/api/admin/resources")
	public ResponseEntity<?> addResources(@RequestBody Set<Resource> els)
	{
		return ResponseEntity.ok(new ResourceResponse().build(resSer.add(els)));
	}
}
