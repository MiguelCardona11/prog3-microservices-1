package com.mssecurity.mssecurity.Controllers;

import com.mssecurity.mssecurity.Repositories.RoleRepository;
import com.mssecurity.mssecurity.Models.Role;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/roles")

public class RolesController {
    @Autowired
    private RoleRepository theRoleRepository;

    // Método GET (TODOS)
    @GetMapping("")
    public List<Role> index(){
        return this.theRoleRepository.findAll();
    }

    // Método GET (SOLO 1)
    @GetMapping("{id}")
    public Role show(@PathVariable String id){
        Role theRole=this.theRoleRepository
                .findById(id)
                .orElse(null);
        return theRole;
    }

    // Método POST
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Role store(@RequestBody Role newRole){
        return this.theRoleRepository.save(newRole);
    }

    // Método PUT
    @PutMapping("{id}")
    public Role update(@PathVariable String id, @RequestBody Role theNewRole){
        Role theActualRole = this.theRoleRepository
                .findById(id)
                .orElse(null);
        if (theActualRole != null) {
            theActualRole.setName(theNewRole.getName());
            theActualRole.setDescription(theNewRole.getDescription());
            return this.theRoleRepository.save(theActualRole);
        } else {
            return null;
        }
    }

    // Método DELETE
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void destroy(@PathVariable String id) {
        Role theRole = this.theRoleRepository
                .findById(id)
                .orElse(null);
        if (theRole != null) {
            this.theRoleRepository.delete(theRole);
        }
    }
}
