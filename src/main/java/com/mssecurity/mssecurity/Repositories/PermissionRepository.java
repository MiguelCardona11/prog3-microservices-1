package com.mssecurity.mssecurity.Repositories;

import com.mssecurity.mssecurity.Models.Permission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface PermissionRepository extends MongoRepository<Permission, String> {

    // CLASE 21/09/2023 (11): método getPermission con URL y metodo (consultando a la BD)
    @Query("{'url':?0,'method':?1}")
    Permission getPermission(String url, String method);
}
