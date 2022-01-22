package io.luverolla.gradi.controllers;

import io.luverolla.gradi.entities.*;
import io.luverolla.gradi.exceptions.InvalidPropertyException;
import io.luverolla.gradi.structures.EntitySetRequest;
import io.luverolla.gradi.services.MessageService;
import io.luverolla.gradi.services.ResourceService;
import io.luverolla.gradi.services.ResourceTypeService;
import io.luverolla.gradi.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
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
        return ResponseEntity.ok(data);
    }

    @GetMapping("/users/{code}")
    public ResponseEntity<?> getUser(@PathVariable("code") String code)
    {
        try {
            User found = userService.get(code);
            return ResponseEntity.ok(found);
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/users")
    public ResponseEntity<?> addUsers(@RequestBody Collection<User> data)
    {
        Set<User> saved = userService.add(data);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/users/{code}")
    public ResponseEntity<?> updateUser(@PathVariable("code") String code, @RequestBody User data)
    {
        try {
            User saved = userService.update(code, data);
            return ResponseEntity.ok(saved);
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
            return ResponseEntity.ok(data);
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
            return ResponseEntity.ok(found);
        }
        catch(NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/resources/{code}/files/{fileCode}")
    public ResponseEntity<?> getFile(@PathVariable("code") String code, @PathVariable("fileCode") String fileCode)
    {
        try {
            Resource found = resourceService.get(code);
            ResourceFile rf = resourceService.getFile(found, fileCode);
            File file = resourceService.getFileObject(rf);

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
            return ResponseEntity.ok(found);
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/resources/{code}/files")
    public ResponseEntity<?> addFile(@PathVariable("code") String code, @RequestParam MultipartFile mpf)
    {
        try {
            Resource found = resourceService.get(code);
            ResourceFile saved = resourceService.addFile(found, mpf);
            return ResponseEntity.ok(saved);
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
            Resource found = resourceService.get(code);
            resourceService.deleteFile(found, fileCode);
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
            Resource found = resourceService.get(resCode);
            ResourceAttribute ra = resourceService.getAttribute(found, propName);
            return ResponseEntity.ok(ra);
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
            return ResponseEntity.ok(attrs);
        }
        catch(NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/resources/{code}/attributes")
    public ResponseEntity<?> addResourceAttributes(@PathVariable("code") String code, @RequestBody Collection<ResourceAttribute> data)
    {
        try {
            Resource found = resourceService.get(code);
            Resource saved = resourceService.addAttributes(found, data);
            return ResponseEntity.ok(saved);
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/resources/{code}/attributes/{prop}")
    public ResponseEntity<?> updateAttribute(@PathVariable("code") String code, @PathVariable("prop") String propName, @RequestBody ResourceAttribute data)
    {
        try {
            Resource found = resourceService.get(code);
            ResourceAttribute saved = resourceService.updateAttribute(found, propName, data);
            return ResponseEntity.ok(saved);
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/resources/{code}/attributes/{prop}")
    public ResponseEntity<?> deleteAttribute(@PathVariable("code") String code, @PathVariable("prop") String propName)
    {
        try {
            Resource found = resourceService.get(code);
            resourceService.deleteAttribute(found, propName);
            return ResponseEntity.noContent().build();
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/resources")
    public ResponseEntity<?> addResources(@RequestBody Collection<Resource> data)
    {
        for(Resource r : data)
        {
            ResourcePermission p = new ResourcePermission();
            p.setType(ResourcePermission.Type.FULL);
            p.setUser(userService.get("0000000000"));
            p.setResource(r);
            r.setPermissions(Set.of(p));
        }

        Set<Resource> saved = resourceService.add(data);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/resources/{code}")
    public ResponseEntity<?> updateResource(@PathVariable("code") String code, @RequestBody Resource data)
    {
        try {
            Resource saved = resourceService.update(code, data);
            return ResponseEntity.ok(saved);
        }
        catch(NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/resources/{code}/permissions")
    public ResponseEntity<?> setPermissions(@PathVariable("code") String code, @RequestBody Set<ResourcePermission> data)
    {
        try {
            Resource found = resourceService.get(code);
            Set<ResourcePermission> saved = resourceService.setPermissions(found, data);
            return ResponseEntity.ok(saved);
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
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
            return ResponseEntity.ok(found);
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
        return ResponseEntity.ok(data);
    }

    @PostMapping("/types")
    public ResponseEntity<?> addResourceTypes(@RequestBody Collection<ResourceType> data)
    {
        Set<ResourceType> saved = resourceTypeService.add(data);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/types/{code}")
    public ResponseEntity<?> updateResourceType(@PathVariable("code") String code, @RequestBody ResourceType type)
    {
        try {
            ResourceType saved = resourceTypeService.update(code, type);
            return ResponseEntity.ok(saved);
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
            return ResponseEntity.ok(found);
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
            return ResponseEntity.ok(props);
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
            return ResponseEntity.ok(saved);
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
            return ResponseEntity.ok(saved);
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
            return ResponseEntity.ok(found);
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<?> getMessages(@RequestBody EntitySetRequest req)
    {
        SortedSet<Message> data = messageService.get(req);
        return ResponseEntity.ok(data);
    }

    @PostMapping("/messages")
    public ResponseEntity<?> addMessages(@RequestBody Collection<Message> data)
    {
        Set<Message> saved = messageService.add(data);
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