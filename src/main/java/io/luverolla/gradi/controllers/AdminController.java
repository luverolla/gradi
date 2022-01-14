package io.luverolla.gradi.controllers;

import io.luverolla.gradi.entities.Resource;
import io.luverolla.gradi.entities.ResourceType;
import io.luverolla.gradi.entities.User;
import io.luverolla.gradi.exceptions.InvalidPropertyException;
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

import java.util.Collection;
import java.util.NoSuchElementException;
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

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestBody EntitySetRequest req)
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
        User found;
        try {
            found = userService.getOneUser(code);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }

        Link self = linkTo(methodOn(AdminController.class).getUser(code)).withSelfRel();
        return ResponseEntity.ok(found.add(self));
    }

    @PostMapping("/users")
    public ResponseEntity<?> addUsers(@RequestBody Collection<User> data)
    {
        Set<User> saved = userService.add(data);
        for(User u : saved)
            u.add(linkTo(methodOn(AdminController.class).getUser(u.getCode())).withSelfRel());

        Link all = linkTo(methodOn(AdminController.class).getAllUsers(EntitySetRequest.simple())).withSelfRel();
        return ResponseEntity.ok(CollectionModel.of(saved, all));
    }

    @PutMapping("/users/{code}")
    public ResponseEntity<?> updateUser(@PathVariable("code") String code, @RequestBody User data)
    {
        User saved;
        try {
            saved = userService.updateOneUser(code, data);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }

        Link self = linkTo(methodOn(AdminController.class).getUser(code)).withSelfRel();
        return ResponseEntity.ok(saved.add(self));
    }

    @DeleteMapping("/users/{code}")
    public ResponseEntity<?> deleteUser(@PathVariable("code") String code)
    {
        try {
            userService.getOneUser(code);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }

        userService.deleteOneUser(code);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/resources")
    public ResponseEntity<?> getAllResources(@RequestBody EntitySetRequest req)
    {
        if(req == null)
            return ResponseEntity.badRequest().build();

        SortedSet<Resource> data;
        try {
            data = resourceService.getResources(req);
        } catch (InvalidPropertyException e) {
            return ResponseEntity.badRequest().build();
        }

        for(Resource r : data)
            r.add(linkTo(methodOn(AdminController.class).getResource(r.getCode())).withSelfRel());

        Link all = linkTo(methodOn(AdminController.class).getAllResources(req)).withSelfRel();
        return ResponseEntity.ok(CollectionModel.of(data, all));
    }

    @GetMapping("/resources/{code}")
    public ResponseEntity<?> getResource(@PathVariable("code") String code)
    {
        Resource found;
        try {
            found = resourceService.getOneResource(code);
        } catch(NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }

        Link self = linkTo(methodOn(AdminController.class).getResource(code)).withSelfRel();
        return ResponseEntity.ok(found.add(self));
    }

    @PostMapping("/resources")
    public ResponseEntity<?> addResources(@RequestBody Collection<Resource> data)
    {
        Set<Resource> saved = resourceService.addResources(data);
        for(Resource r : saved)
            r.add(linkTo(methodOn(AdminController.class).getResource(r.getCode())).withSelfRel());

        Link all = linkTo(methodOn(AdminController.class).getAllResources(null)).withSelfRel();
        return ResponseEntity.ok(CollectionModel.of(saved, all));
    }

    @PutMapping("/resources/{code}")
    public ResponseEntity<?> updateResource(@PathVariable("code") String code, @RequestBody Resource data)
    {
        Resource saved;
        try {
            saved = resourceService.updateOneResource(code, data);
        } catch(NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Link self = linkTo(methodOn(AdminController.class).getResource(code)).withSelfRel();
        return ResponseEntity.ok(saved.add(self));
    }

    @DeleteMapping("/resources/{code}")
    public ResponseEntity<?> deleteResource(@PathVariable("code") String code)
    {
        try {
            resourceService.getOneResource(code);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }

        resourceService.deleteOneResource(code);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/resource-types/{code}")
    public ResponseEntity<?> getResourceType(@PathVariable("code") String code)
    {
        ResourceType found;
        try {
            found = resourceTypeService.getOneType(code);
        } catch(NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }

        Link self = linkTo(methodOn(AdminController.class).getResourceType(code)).withSelfRel();
        return ResponseEntity.ok(found.add(self));
    }

    @GetMapping("/resources-types")
    public ResponseEntity<?> getAllResourcesTypes(@RequestBody EntitySetRequest req)
    {
        if(req == null)
            return ResponseEntity.badRequest().build();

        SortedSet<ResourceType> data = resourceTypeService.getTypes(req);
        for(ResourceType t : data)
            t.add(linkTo(methodOn(AdminController.class).getResourceType(t.getCode())).withSelfRel());

        Link all = linkTo(methodOn(AdminController.class).getAllResourcesTypes(req)).withSelfRel();
        return ResponseEntity.ok(CollectionModel.of(data, all));
    }

    @PostMapping("/resource-types")
    public ResponseEntity<?> addResourceTypes(@RequestBody Collection<ResourceType> data)
    {
        Set<ResourceType> saved = resourceTypeService.addTypes(data);
        for(ResourceType t : saved)
            t.add(linkTo(methodOn(AdminController.class).getResourceType(t.getCode())).withSelfRel());

        Link all = linkTo(methodOn(AdminController.class).getAllResourcesTypes(EntitySetRequest.simple())).withSelfRel();
        return ResponseEntity.ok(CollectionModel.of(saved, all));
    }

    @PutMapping("/resource-types/{code}")
    public ResponseEntity<?> updateResourceType(@PathVariable("code") String code, @RequestBody ResourceType type)
    {
        ResourceType saved;
        try {
            saved = resourceTypeService.updateType(code, type);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }

        Link self = linkTo(methodOn(AdminController.class).getResourceType(code)).withSelfRel();
        return ResponseEntity.ok(saved.add(self));
    }

    @DeleteMapping("/resources/{code}")
    public ResponseEntity<?> deleteResourceType(@PathVariable("code") String code)
    {
        try {
            resourceTypeService.delete(code);
        }
        catch(NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
