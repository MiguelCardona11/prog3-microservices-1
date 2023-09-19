package com.mssecurity.mssecurity.Controllers;

import com.mssecurity.mssecurity.Models.Permission;
import com.mssecurity.mssecurity.Models.Role;
import com.mssecurity.mssecurity.Models.RolePermission;
import com.mssecurity.mssecurity.Repositories.PermissionRepository;
import com.mssecurity.mssecurity.Repositories.RolePermissionRepository;
import com.mssecurity.mssecurity.Repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
// CLASE 14/09/2023 (9): cambiamos la ruta de ("/role-permission") a ("api/role-permission")
@RequestMapping("api/role-permission")

public class RolePermissionsController {
    @Autowired
    //Permite crear la entidad en la base de datos
    private RolePermissionRepository theRolePermissionRepository;

    // Referencia al repositorio de Role y Permission para poder buscar el rol y el permiso en la BD
    @Autowired
    private RoleRepository theRoleRepository;

    @Autowired
    private PermissionRepository thePermissionRepository;

    // Método POST
    // Para poder crear la relacion necesito mandar los id de las clases referenciadas en esta clase intermedia (se pueden mandar en la URL o en el body, en este caso en la URL)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("role/{role_id}/permission/{permission_id}")
    public RolePermission store(@PathVariable String role_id,
                                @PathVariable String permission_id) {
        // Buscamos el Rol y el permiso en la BD con su identificador role_id y permission_id
        Role theRole=theRoleRepository.findById(role_id).orElse(null);
        Permission thePermission=thePermissionRepository.findById(permission_id).orElse(null);
        // Comprobamos que si existen
        if(theRole!=null && thePermission!=null){
            // Creamos el RolePermission con el constructor vacío.
            RolePermission newRolePermission = new RolePermission();
            // Le seteamos los valores al objeto ya que no lo hicimos en el cosntructor
            newRolePermission.setRole(theRole);
            newRolePermission.setPermission(thePermission);
            // Guardamos en la DB
            return this.theRolePermissionRepository.save(newRolePermission);
        } else {
            return null;
        }
    }
    // Método GET (TODOS)
    @GetMapping("")
    public List<RolePermission> index(){
        return this.theRolePermissionRepository.findAll();
    }

    // Método DELETE
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void destroy(@PathVariable String id) {
        RolePermission theRoleRolePermission = this.theRolePermissionRepository
                .findById(id)
                .orElse(null);
        if (theRoleRolePermission != null) {
            this.theRolePermissionRepository.delete(theRoleRolePermission);
        }
    }


}
