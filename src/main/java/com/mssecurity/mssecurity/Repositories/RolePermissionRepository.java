package com.mssecurity.mssecurity.Repositories;

// import Models.Role;
import com.mssecurity.mssecurity.Models.RolePermission;
// import Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface RolePermissionRepository extends MongoRepository<RolePermission, String> {

    // CLASE 21/09/2023 (11): definir metodo para encontrar RolePermission con rol y permiso (consultando a la BD)
    @Query("{'role.$id': ObjectId(?0),'permission.$id': ObjectId(?1)}")
    RolePermission getRolePermission(String roleId,String permissionId);
}
