package com.mssecurity.mssecurity.Repositories;

import com.mssecurity.mssecurity.Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

// La interfaz implementa algo de mongo para poder manipular a los usuarios, y es string porque el identificador del usuario es un string
public interface UserRepository extends MongoRepository<User,String> {

    // CLASE 12/09/2023 (8) Implementación de una consulta para buscar un usuario por su correo
    // Consulta hacia la bases de datos de Mongo
    // buscar en mongo lo que concuerde con email lo que voy a mandar por parámetro
    @Query("{'email': ?0}")
    // devuelve un User si me dan un email
    public User getUserByEmail(String email);
}
