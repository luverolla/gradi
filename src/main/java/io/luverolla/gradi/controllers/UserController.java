package io.luverolla.gradi.controllers;

import io.luverolla.gradi.entities.*;
import io.luverolla.gradi.entities.ResourcePermission.*;
import io.luverolla.gradi.exceptions.InvalidPropertyException;
import io.luverolla.gradi.rest.EntitySetRequest;
import io.luverolla.gradi.services.MessageService;
import io.luverolla.gradi.services.ResourceService;
import io.luverolla.gradi.services.ResourceTypeService;
import io.luverolla.gradi.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.security.Principal;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;

@RequestMapping("/api/user")
@RestController
public class UserController
{
    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ResourceTypeService resourceTypeService;

    @PutMapping("/password-reset")
    public ResponseEntity<?> resetPassword(Principal pr)
    {
        User u = userService.get(pr);
        userService.passwordReset(u);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestBody EntitySetRequest req)
    {
        if(req == null)
            return ResponseEntity.badRequest().build();

        SortedSet<User> data = userService.get(req);

        // normal user should not see admin details
        data.remove(userService.getAdmin());

        return ResponseEntity.ok(data);
    }

    @GetMapping("/users/{code}")
    public ResponseEntity<?> getUser(@PathVariable("code") String code)
    {
        try {
            User found = userService.get(code);
            return found.isAdmin() ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok(found);
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/resources")
    public ResponseEntity<?> getAllResources(Principal pr, @RequestBody EntitySetRequest req)
    {
        if(req == null)
            return ResponseEntity.badRequest().build();

        try {
            User u = userService.get(pr);
            SortedSet<Resource> data = resourceService.get(u, ResourcePermission.Type.READ, req);
            return ResponseEntity.ok(data);
        }
        catch (InvalidPropertyException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/resources/{code}")
    public ResponseEntity<?> getResource(Principal pr, @PathVariable("code") String code)
    {
        try {
            User u = userService.get(pr);
            Resource found = resourceService.get(u, Type.READ, code);
            return ResponseEntity.ok(found);
        }
        catch(NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/resources/{code}/files/{fileCode}")
    public ResponseEntity<?> getFile(Principal pr, @PathVariable("code") String code, @PathVariable("fileCode") String fileCode)
    {
        try {
            User u = userService.get(pr);
            Resource res = resourceService.get(u, Type.READ, code);
            ResourceFile found = resourceService.getFile(res, fileCode);
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
    public ResponseEntity<?> getFiles(Principal pr, @PathVariable("code") String code)
    {
        try {
            User u = userService.get(pr);
            Set<ResourceFile> found = resourceService.get(u, Type.READ, code).getFiles();
            return ResponseEntity.ok(found);
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('EDITOR')")
    @PostMapping("/resources/{code}/files")
    public ResponseEntity<?> addFile(Principal pr, @PathVariable("code") String code, @RequestParam MultipartFile mpf)
    {
        try {
            User u = userService.get(pr);
            Resource found = resourceService.get(u, Type.WRITE, code);
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

    @PreAuthorize("hasRole('EDITOR')")
    @DeleteMapping("/resources/{code}/files/{fileCode}")
    public ResponseEntity<?> deleteFile(Principal pr, @PathVariable("code") String code, @PathVariable("fileCode") String fileCode)
    {
        try {
            User u = userService.get(pr);
            Resource found = resourceService.get(u, Type.WRITE, code);
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
    public ResponseEntity<?> getAttribute(Principal pr, @PathVariable("code") String resCode, @PathVariable("prop") String propName)
    {
        try {
            User u = userService.get(pr);
            Resource res = resourceService.get(u, Type.READ, resCode);
            ResourceAttribute found = resourceService.getAttribute(res, propName);
            return ResponseEntity.ok(found);
        }
        catch(NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/resources/{code}/attributes")
    public ResponseEntity<?> getResourceAttributes(Principal pr, @PathVariable("code") String code)
    {
        try {
            User u = userService.get(pr);
            Set<ResourceAttribute> attrs = resourceService.get(u, Type.READ, code).getAttributes();
            return ResponseEntity.ok(attrs);
        }
        catch(NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('EDITOR')")
    @PostMapping("/resources/{code}/attributes")
    public ResponseEntity<?> addResourceAttributes(Principal pr, @PathVariable("code") String code, @RequestBody Collection<ResourceAttribute> data)
    {
        try {
            User u = userService.get(pr);
            Resource found = resourceService.get(u, Type.WRITE, code);
            Resource saved = resourceService.addAttributes(found, data);
            return ResponseEntity.ok(saved);
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('EDITOR')")
    @PutMapping("/resources/{code}/attributes/{prop}")
    public ResponseEntity<?> updateAttribute(Principal pr, @PathVariable("code") String code, @PathVariable("prop") String propName, @RequestBody ResourceAttribute data)
    {
        try {
            User u = userService.get(pr);
            Resource found = resourceService.get(u, Type.WRITE, code);
            ResourceAttribute saved = resourceService.updateAttribute(found, propName, data);
            return ResponseEntity.ok(saved);
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('EDITOR')")
    @DeleteMapping("/resources/{code}/attributes/{prop}")
    public ResponseEntity<?> deleteAttribute(Principal pr, @PathVariable("code") String code, @PathVariable("prop") String propName)
    {
        try {
            User u = userService.get(pr);
            Resource found = resourceService.get(u, Type.WRITE, code);
            resourceService.deleteAttribute(found, propName);
            return ResponseEntity.noContent().build();
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('EDITOR')")
    @PostMapping("/resources")
    public ResponseEntity<?> addResources(Principal pr, @RequestBody Collection<Resource> data)
    {
        for(Resource r : data)
        {
            ResourcePermission p = new ResourcePermission();
            p.setType(ResourcePermission.Type.FULL);
            p.setUser(userService.get(pr));
            p.setResource(r);
            r.setPermissions(Set.of(p));
        }

        Set<Resource> saved = resourceService.add(data);
        return ResponseEntity.ok(saved);
    }

    @PreAuthorize("hasRole('EDITOR')")
    @PutMapping("/resources/{code}")
    public ResponseEntity<?> updateResource(Principal pr, @PathVariable("code") String code, @RequestBody Resource data)
    {
        try {
            User u = userService.get(pr);
            resourceService.get(u, Type.WRITE, code); // needed to trigger exception
            Resource saved = resourceService.update(code, data);
            return ResponseEntity.ok(saved);
        }
        catch(NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PreAuthorize("hasRole('EDITOR')")
    @PutMapping("/resources/{code}/permissions")
    public ResponseEntity<?> setPermissions(Principal pr, @PathVariable("code") String code, @RequestBody Set<ResourcePermission> data)
    {
        try {
            User u = userService.get(pr);
            Resource found = resourceService.get(u, Type.FULL, code);
            Set<ResourcePermission> saved = resourceService.setPermissions(found, data);
            return ResponseEntity.ok(saved);
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('EDITOR')")
    @DeleteMapping("/resources/{code}")
    public ResponseEntity<?> deleteResource(Principal pr, @PathVariable("code") String code)
    {
        try {
            User u = userService.get(pr);
            resourceService.get(u, Type.FULL, code); // needed to trigger exception
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
    public ResponseEntity<?> getMessages(Principal pr, @RequestBody EntitySetRequest req)
    {
        User u = userService.get(pr);
        SortedSet<Message> data = messageService.get(u, req);
        return ResponseEntity.ok(data);
    }
}
