package Repositories;

import Models.Role;
import Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role,String> {

}
