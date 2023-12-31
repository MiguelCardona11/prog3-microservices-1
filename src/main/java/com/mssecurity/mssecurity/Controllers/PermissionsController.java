package com.mssecurity.mssecurity.Controllers;

import com.mssecurity.mssecurity.Repositories.PermissionRepository;
import com.mssecurity.mssecurity.Models.Permission;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
// CLASE 14/09/2023 (9): cambiamos la ruta de ("/permissions") a ("api/permissions")
@RequestMapping("api/permissions")

public class PermissionsController {
    @Autowired
    private PermissionRepository thePermissionRepository;

    // Método GET (TODOS)
    @GetMapping("")
    public List<Permission> index() {
        return this.thePermissionRepository.findAll();
    }

    // Método GET (SOLO 1)
    @GetMapping("{id}")
    public Permission show(@PathVariable String id) {
        Permission thePermission = this.thePermissionRepository
                .findById(id)
                .orElse(null);
        return thePermission;
    }

    // Método POST
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Permission store(@RequestBody Permission newPermission) {
        return this.thePermissionRepository.save(newPermission);
    }

    // Método PUT
    @PutMapping("{id}")
    public Permission update(@PathVariable String id, @RequestBody Permission theNewPermission) {
        Permission theActualPermission = this.thePermissionRepository
                .findById(id)
                .orElse(null);
        if (theActualPermission != null) {
            theActualPermission.setUrl(theNewPermission.getUrl());
            theActualPermission.setMethod(theNewPermission.getMethod());
            theActualPermission.setMenuItem(theNewPermission.getMenuItem());
            return this.thePermissionRepository.save(theActualPermission);
        } else {
            return null;
        }
    }

    // Método DELETE
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void destroy(@PathVariable String id) {
        Permission thePermission = this.thePermissionRepository
                .findById(id)
                .orElse(null);
        if (thePermission != null) {
            this.thePermissionRepository.delete(thePermission);
        }
    }
}
