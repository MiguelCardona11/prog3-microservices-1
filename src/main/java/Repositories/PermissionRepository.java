package Repositories;

import Models.Permission;
import Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PermissionRepository extends MongoRepository<Permission,String> {

}
