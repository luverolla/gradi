package io.luverolla.gradi.controllers;

import io.luverolla.gradi.entities.User;
import io.luverolla.gradi.rest.EntitySetRequest;
import io.luverolla.gradi.services.ResourceService;
import io.luverolla.gradi.services.ResourceTypeService;
import io.luverolla.gradi.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.SortedSet;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/admin")
public class AdminController
{
	@Autowired
    private UserService userService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ResourceTypeService resourceTypeService;

    @PutMapping("/password-reset")
    public ResponseEntity<?> passwordReset()
    {
        userService.passwordReset(userService.getAdmin());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestBody EntitySetRequest<User> req)
    {
        if(req == null)
            return ResponseEntity.badRequest().build();

        SortedSet<User> data = userService.getUsers(req);
        for(User u : data)
            u.add(linkTo(methodOn(AdminController.class).getUser(u.getCode())).withSelfRel());

        Link all = linkTo(methodOn(AdminController.class).getAllUsers(req)).withSelfRel();
        return ResponseEntity.ok(CollectionModel.of(data, all));
    }

    @GetMapping("/users/{code}")
    public ResponseEntity<?> getUser(@PathVariable("code") String code)
    {
        User found = userService.getOneUser(code);
        if(found == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        Link self = linkTo(methodOn(AdminController.class).getUser(code)).withSelfRel();
        return ResponseEntity.ok(found.add(self));
    }

    @PostMapping("/users")
    public ResponseEntity<?> addUsers(@RequestBody Set<User> data)
    {
        Set<User> saved = userService.add(data);
        for(User u : saved)
            u.add(linkTo(methodOn(AdminController.class).getUser(u.getCode())).withSelfRel());

        Link all = linkTo(methodOn(AdminController.class).getAllUsers(null)).withSelfRel();
        return ResponseEntity.ok(CollectionModel.of(saved, all));
    }

    @PutMapping("/users/{code}")
    public ResponseEntity<?> updateUser(@PathVariable("code") String code, @RequestBody User data)
    {
        User saved = userService.updateOneUser(code, data);
        if(saved == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        Link self = linkTo(methodOn(AdminController.class).getUser(code)).withSelfRel();
        return ResponseEntity.ok(saved.add(self));
    }

    @DeleteMapping("/users/{code}")
    public ResponseEntity<?> deleteUser(@PathVariable("code") String code)
    {
        User found = userService.getOneUser(code);
        if(found == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        userService.deleteOneUser(code);
        return ResponseEntity.noContent().build();
    }
}
