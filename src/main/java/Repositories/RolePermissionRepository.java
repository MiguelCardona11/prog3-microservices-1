package Repositories;

import Models.Role;
import Models.RolePermission;
import Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RolePermissionRepository extends MongoRepository<RolePermission,String> {

}
