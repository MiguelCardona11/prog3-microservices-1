package com.mssecurity.mssecurity.Repositories;

import com.mssecurity.mssecurity.Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

// La interfaz implementa algo de mongo para poder manipular a los usuarios, y es string porque el identificador del usuario es un string
public interface UserRepository extends MongoRepository<User,String> {

}
