package com.mssecurity.mssecurity.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

public class RolePermission {
    @Id
    // El guión bajo es debido a que así son los identificadores por defecto
    private String _id;
    @DBRef
    private Role role;
    @DBRef
    private Permission permission;

    public RolePermission() {
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }
}
