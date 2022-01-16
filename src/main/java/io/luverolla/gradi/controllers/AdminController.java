package io.luverolla.gradi.controllers;

import io.luverolla.gradi.entities.*;
import io.luverolla.gradi.exceptions.InvalidPropertyException;
import io.luverolla.gradi.rest.EntitySetRequest;
import io.luverolla.gradi.services.MessageService;
import io.luverolla.gradi.services.ResourceService;
import io.luverolla.gradi.services.ResourceTypeService;
import io.luverolla.gradi.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
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
    private MessageService messageService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ResourceTypeService resourceTypeService;

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestBody EntitySetRequest req)
    {
        if(req == null)
            return ResponseEntity.badRequest().build();

        SortedSet<User> data = userService.get(req);
        for(User u : data)
            u.add(linkTo(methodOn(AdminController.class).getUser(u.getCode())).withSelfRel());

        Link all = linkTo(methodOn(AdminController.class).getAllUsers(req)).withSelfRel();
        return ResponseEntity.ok(CollectionModel.of(data, all));
    }

    @GetMapping("/users/{code}")
    public ResponseEntity<?> getUser(@PathVariable("code") String code)
    {
        try {
            User found = userService.get(code);

            Link self = linkTo(methodOn(AdminController.class).getUser(code)).withSelfRel();
            return ResponseEntity.ok(found.add(self));
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/users")
    public ResponseEntity<?> addUsers(@RequestBody Collection<User> data)
    {
        Set<User> saved = userService.add(data);
        for(User u : saved)
            u.add(linkTo(methodOn(AdminController.class).getUser(u.getCode())).withSelfRel());

        return ResponseEntity.ok(saved);
    }

    @PutMapping("/users/{code}")
    public ResponseEntity<?> updateUser(@PathVariable("code") String code, @RequestBody User data)
    {
        try {
            User saved = userService.update(code, data);

            Link self = linkTo(methodOn(AdminController.class).getUser(code)).withSelfRel();
            return ResponseEntity.ok(saved.add(self));
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/users/{code}")
    public ResponseEntity<?> deleteUser(@PathVariable("code") String code)
    {
        try {
            userService.delete(code);
            return ResponseEntity.noContent().build();
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/resources")
    public ResponseEntity<?> getAllResources(@RequestBody EntitySetRequest req)
    {
        if(req == null)
            return ResponseEntity.badRequest().build();

        try {
            SortedSet<Resource> data = resourceService.get(req);
            for(Resource r : data)
                r.add(linkTo(methodOn(AdminController.class).getResource(r.getCode())).withSelfRel());

            Link all = linkTo(methodOn(AdminController.class).getAllResources(req)).withSelfRel();
            return ResponseEntity.ok(CollectionModel.of(data, all));
        }
        catch (InvalidPropertyException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/resources/{code}")
    public ResponseEntity<?> getResource(@PathVariable("code") String code)
    {
        try {
            Resource found = resourceService.get(code);
            Link self = linkTo(methodOn(AdminController.class).getResource(code)).withSelfRel();
            return ResponseEntity.ok(found.add(self));
        }
        catch(NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/resources/{code}/files/{fileCode}")
    public ResponseEntity<?> getFile(@PathVariable("code") String code, @PathVariable("fileCode") String fileCode)
    {
        try {
            ResourceFile found = resourceService.getFile(code, fileCode);
            File file = resourceService.getFileObject(found);

            byte[] bytes = Files.readAllBytes(file.toPath());
            String type = URLConnection.guessContentTypeFromName(found.getName());

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + found.getName());
            headers.add(HttpHeaders.CONTENT_LENGTH, Long.toString(file.length()));
            headers.add(HttpHeaders.CONTENT_TYPE, type);

            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/resources/{code}/files")
    public ResponseEntity<?> getFiles(@PathVariable("code") String code)
    {
        try {
            Set<ResourceFile> found = resourceService.get(code).getFiles();
            for(ResourceFile f : found)
                f.add(linkTo(methodOn(AdminController.class).getFile(code, f.getCode())).withSelfRel());

            Link all = linkTo(methodOn(AdminController.class).getFiles(code)).withSelfRel();
            return ResponseEntity.ok(CollectionModel.of(found, all));
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/resources/{code}/files")
    public ResponseEntity<?> addFile(@PathVariable("code") String code, @RequestParam MultipartFile mpf)
    {
        try {
            ResourceFile saved = resourceService.addFile(code, mpf);

            Link self = linkTo(methodOn(AdminController.class).getFile(code, saved.getCode())).withSelfRel();
            return ResponseEntity.ok(saved.add(self));
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/resources/{code}/files/{fileCode}")
    public ResponseEntity<?> deleteFile(@PathVariable("code") String code, @PathVariable("fileCode") String fileCode)
    {
        try {
            resourceService.deleteFile(code, fileCode);
            return ResponseEntity.noContent().build();
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/resources/{code}/attributes/{prop}")
    public ResponseEntity<?> getAttribute(@PathVariable("code") String resCode, @PathVariable("prop") String propName)
    {
        try {
            ResourceAttribute found = resourceService.getAttribute(resCode, propName);
            Link self = linkTo(methodOn(AdminController.class).getAttribute(resCode, propName)).withSelfRel();
            return ResponseEntity.ok(found.add(self));
        }
        catch(NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/resources/{code}/attributes")
    public ResponseEntity<?> getResourceAttributes(@PathVariable("code") String code)
    {
        try {
            Set<ResourceAttribute> attrs = resourceService.get(code).getAttributes();
            for(ResourceAttribute a : attrs)
                a.add(linkTo(methodOn(AdminController.class).getAttribute(code, a.getProperty().getName())).withSelfRel());

            Link all = linkTo(methodOn(AdminController.class).getResourceAttributes(code)).withSelfRel();
            return ResponseEntity.ok(CollectionModel.of(attrs, all));
        }
        catch(NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/resources/{code}/attributes")
    public ResponseEntity<?> addResourceAttributes(@PathVariable("code") String code, @RequestBody Collection<ResourceAttribute> data)
    {
        try {
            Set<ResourceAttribute> saved = resourceService.addAttributes(code, data);
            for(ResourceAttribute a : data)
                a.add(linkTo(methodOn(AdminController.class).getAttribute(code, a.getProperty().getName())).withSelfRel());

            Link all = linkTo(methodOn(AdminController.class).getResourceAttributes(code)).withSelfRel();
            return ResponseEntity.ok(CollectionModel.of(saved, all));
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/resources/{code}/attributes/{prop}")
    public ResponseEntity<?> updateAttribute(@PathVariable("code") String code, @PathVariable("prop") String propName, @RequestBody ResourceAttribute data)
    {
        try {
            ResourceAttribute saved = resourceService.updateAttribute(code, propName, data);

            Link self = linkTo(methodOn(AdminController.class).getAttribute(code, propName)).withSelfRel();
            return ResponseEntity.ok(saved.add(self));
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/resources/{code}/attributes/{prop}")
    public ResponseEntity<?> deleteAttribute(@PathVariable("code") String code, @PathVariable("prop") String propName)
    {
        try {
            resourceService.deleteAttribute(code, propName);
            return ResponseEntity.noContent().build();
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/resources")
    public ResponseEntity<?> addResources(@RequestBody Collection<Resource> data)
    {
        Set<Resource> saved = resourceService.add(data);
        for(Resource r : saved)
            r.add(linkTo(methodOn(AdminController.class).getResource(r.getCode())).withSelfRel());

        return ResponseEntity.ok(saved);
    }

    @PutMapping("/resources/{code}")
    public ResponseEntity<?> updateResource(@PathVariable("code") String code, @RequestBody Resource data)
    {
        try {
            Resource saved = resourceService.update(code, data);

            Link self = linkTo(methodOn(AdminController.class).getResource(code)).withSelfRel();
            return ResponseEntity.ok(saved.add(self));
        }
        catch(NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/resources/{code}")
    public ResponseEntity<?> deleteResource(@PathVariable("code") String code)
    {
        try {
            resourceService.delete(code);
            return ResponseEntity.noContent().build();
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/types/{code}")
    public ResponseEntity<?> getResourceType(@PathVariable("code") String code)
    {
        try {
            ResourceType found = resourceTypeService.get(code);

            Link self = linkTo(methodOn(AdminController.class).getResourceType(code)).withSelfRel();
            return ResponseEntity.ok(found.add(self));
        }
        catch(NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/types")
    public ResponseEntity<?> getResourcesTypes(@RequestBody EntitySetRequest req)
    {
        if(req == null)
            return ResponseEntity.badRequest().build();

        SortedSet<ResourceType> data = resourceTypeService.get(req);
        for(ResourceType t : data)
            t.add(linkTo(methodOn(AdminController.class).getResourceType(t.getCode())).withSelfRel());

        Link all = linkTo(methodOn(AdminController.class).getResourcesTypes(req)).withSelfRel();
        return ResponseEntity.ok(CollectionModel.of(data, all));
    }

    @PostMapping("/types")
    public ResponseEntity<?> addResourceTypes(@RequestBody Collection<ResourceType> data)
    {
        Set<ResourceType> saved = resourceTypeService.add(data);
        for(ResourceType t : saved)
            t.add(linkTo(methodOn(AdminController.class).getResourceType(t.getCode())).withSelfRel());

        return ResponseEntity.ok(saved);
    }

    @PutMapping("/types/{code}")
    public ResponseEntity<?> updateResourceType(@PathVariable("code") String code, @RequestBody ResourceType type)
    {
        try {
            ResourceType saved = resourceTypeService.update(code, type);

            Link self = linkTo(methodOn(AdminController.class).getResourceType(code)).withSelfRel();
            return ResponseEntity.ok(saved.add(self));
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/types/{code}")
    public ResponseEntity<?> deleteResourceType(@PathVariable("code") String code)
    {
        try {
            resourceTypeService.delete(code);
            return ResponseEntity.noContent().build();
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/types/{code}/properties/{prop}")
    public ResponseEntity<?> getProperty(@PathVariable("code") String code, @PathVariable("prop") String propName)
    {
        try {
            ResourceProperty found = resourceTypeService.getProperty(code, propName);

            Link self = linkTo(methodOn(AdminController.class).getProperty(code, propName)).withSelfRel();
            return ResponseEntity.ok(found.add(self));
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/types/{code}/properties")
    public ResponseEntity<?> getProperties(@PathVariable("code") String code)
    {
        try {
            ResourceType found = resourceTypeService.get(code);

            Set<ResourceProperty> props = found.getProperties();
            for(ResourceProperty p : props) {
                String purePropName = p.getName().split("#")[1];
                p.add(linkTo(methodOn(AdminController.class).getProperty(code, purePropName)).withSelfRel());
            }

            Link all = linkTo(methodOn(AdminController.class).getProperties(code)).withSelfRel();
            return ResponseEntity.ok(CollectionModel.of(props, all));
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/types/{code}/properties")
    public ResponseEntity<?> addProperties(@PathVariable("code") String code, @RequestBody Collection<ResourceProperty> data)
    {
        try {
            Set<ResourceProperty> saved = resourceTypeService.addProperties(code, data);
            for(ResourceProperty p : saved) {
                String purePropName = p.getName().split("#")[1];
                p.add(linkTo(methodOn(AdminController.class).getProperty(code, purePropName)).withSelfRel());
            }

            Link all = linkTo(methodOn(AdminController.class).getProperties(code)).withSelfRel();
            return ResponseEntity.ok(CollectionModel.of(saved, all));
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/types/{code}/properties/{name}")
    public ResponseEntity<?> updateProperty(@PathVariable("code") String code, @PathVariable("name") String propName, @RequestBody ResourceProperty data)
    {
        try {
            ResourceProperty saved = resourceTypeService.updateProperty(code, propName, data);

            Link self = linkTo(methodOn(AdminController.class).getProperty(code, propName)).withSelfRel();
            return ResponseEntity.ok(saved.add(self));
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/types/{code}/properties/{name}")
    public ResponseEntity<?> deleteProperty(@PathVariable("code") String code, @PathVariable("name") String propName)
    {
        try {
            resourceTypeService.deleteProperty(code, propName);
            return ResponseEntity.noContent().build();
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/messages/{code}")
    public ResponseEntity<?> getMessage(@PathVariable("code") String code)
    {
        try {
            Message found = messageService.get(code);

            Link self = linkTo(methodOn(AdminController.class).getMessage(code)).withSelfRel();
            return ResponseEntity.ok(found.add(self));
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<?> getMessages(@RequestBody EntitySetRequest req)
    {
        SortedSet<Message> data = messageService.get(req);
        for(Message m : data)
            m.add(linkTo(methodOn(AdminController.class).getMessage(m.getCode())).withSelfRel());

        Link all = linkTo(methodOn(AdminController.class).getMessages(req)).withSelfRel();
        return ResponseEntity.ok(CollectionModel.of(data, all));
    }

    @PostMapping("/messages")
    public ResponseEntity<?> addMessages(@RequestBody Collection<Message> data)
    {
        Set<Message> saved = messageService.add(data);
        for(Message m : saved)
            m.add(linkTo(methodOn(AdminController.class).getMessage(m.getCode())).withSelfRel());

        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/messages/{code}")
    public ResponseEntity<?> deleteMessage(@PathVariable("code") String code)
    {
        try {
            messageService.delete(code);
            return ResponseEntity.noContent().build();
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}