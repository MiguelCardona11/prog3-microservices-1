package com.mssecurity.mssecurity.Repositories;

// import Models.Role;
import com.mssecurity.mssecurity.Models.RolePermission;
// import Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RolePermissionRepository extends MongoRepository<RolePermission, String> {

}
