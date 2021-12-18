package io.luverolla.gradi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.luverolla.gradi.entities.User;
import io.luverolla.gradi.rest.EntitySetRequest;
import io.luverolla.gradi.services.UserService;

@RestController
public class AdminController
{
	@Autowired
	private UserService ser;
	
	@GetMapping("/api/admin/users")
	public ResponseEntity<?> getAllUsers(@RequestBody EntitySetRequest<User> req)
	{
		return ResponseEntity.ok(ser.get(req));
	}
}
