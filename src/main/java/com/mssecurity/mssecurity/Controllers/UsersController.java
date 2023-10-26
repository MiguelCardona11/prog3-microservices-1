package com.mssecurity.mssecurity.Controllers;

// Importo la clase Role (CLASE 6)
import com.mssecurity.mssecurity.Models.Role;
import com.mssecurity.mssecurity.Models.User;
import com.mssecurity.mssecurity.Repositories.RoleRepository;
import com.mssecurity.mssecurity.Repositories.UserRepository;
import com.mssecurity.mssecurity.Services.EncryptionService;
import com.mssecurity.mssecurity.Services.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
// CLASE 07/09/2023 (7)
// Importaciones para cifrar las contraseñas del usuario
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

// Este decorador hace que nos conectemos a hacer pruebas de springboot desde la misma máquina (para que no de problemas de cross)
@CrossOrigin
// Este decorador permite usar los CRUD
@RestController
// Los metodos de esta clase se van a activar cuando el usuario ponga en la ruta
// CLASE 14/09/2023 (9): cambiamos la ruta de ("/users") a ("api/users")
@RequestMapping("api/users")

public class UsersController {
    // Necesito una referencia a userRepository
    @Autowired
    // Estamos haciendo una inyección de dependencias.
    // Significa que voy a depender de una interfaz para actuar.
    // El "theUserRepository" hace que aún si cambiamos la interfaz a la que le
    // hacemos referencia (para heredar sus métodos) en el reposiroty de Mongo a
    // MySQL por ejemplo, no afectará acá al controlador.
    private UserRepository theUserRepository;

    // Añadimos el RolRepository (CLASE 6)
    @Autowired
    private RoleRepository theRoleRepository;

    // CLASE 12/09/2023 (8) ahora en vez de tener el metodo de encripcion en la misma clase, la traemos el servicio
    @Autowired
    private EncryptionService encryptionService;

    // Método GET
    // Se va a activar cuando el cliente use un GET
    @GetMapping("")
    // Devuelve una lista de todos los usuarios que están en la clase usuarios. (Al
    // usar "User" estamos interactuando con el Modelo)
    public List<User> index() {
        // (Me estoy conectando con el repositorio, que es el que tiene el CRUD)
        // El repositorio es el que se comunica con la base de datos (que se crea sola
        // en springboot), entonces nos retorna todos los usuarios de la BD con el
        // método findAll que la interfaz heredó de Mongo.
        return this.theUserRepository.findAll();
    }

    // Método POST (store)
    // Respuesta para cuando funciona
    @ResponseStatus(HttpStatus.CREATED)
    // Se va a activar cuando el cliente use el POST
    @PostMapping

    // Voy a devolver un User, viene un parametro con un Generic (el va a fijarse en
    // el pedazo "body" del JSON, y lo casteamos en el formato de la clase User)
    public User store(@RequestBody User newUser) {
        // CLASE 07/09/2023 (7) (implementacion de cifrado)
        newUser.setPassword(encryptionService.convertirSHA256(newUser.getPassword()));

        // le digo al repositorio que me guarde el usuario, y este lo guarda en Mongo
        return this.theUserRepository.save(newUser);
        // Automáticamente lo retorna en formato JSON.
    }

    // Este metodo es para ver info de 1 solo usuario
    // Por lo tanto es una ampliación del método GET
    // Envío en la URL un identificador
    @GetMapping("{id}")
    // Devuelve un Usuario. Leo el identificador del nombre que viene en la ruta.
    public User show(@PathVariable String id) {
        // busco al usuario por su identificador en la DB.
        User theUser = this.theUserRepository
                // Le pregunto al repositorio que si puede buscar a un usuario por su
                // identificador
                .findById(id)
                // Si no lo encuentra, devuelva un null
                .orElse(null);
        // Retorne la información del usuario
        return theUser;
    }

    // Método para actualización (PUT)
    // Seleccionamos el metodo para PUT con el "@" que recibira el identificador
    @PutMapping("{id}")
    // Devolverá un usuario. Leemos el identificador que viene en la ruta con el
    // @PathVariable (usuario antiguo). Y luego necesito la información del nuevo
    // usuario, lo que va en el body
    public User update(@PathVariable String id, @RequestBody User theNewUser) {
        // El usuario actual es el que esta en la base de datos (Se llega a esta con el
        // Repository)
        User theActualUser = this.theUserRepository
                .findById(id)
                .orElse(null);
        // Si el usuario actual existe
        if (theActualUser != null) {
            // Le actualizo al Usuario actual todos los datos nuevos que se hayan puesto en
            // el body
            theActualUser.setName(theNewUser.getName());
            theActualUser.setEmail(theNewUser.getEmail());
            // CLASE 07/09/2023 (7) ciframos la contraseña
            theActualUser.setPassword(encryptionService.convertirSHA256(theNewUser.getPassword()));
            // Le digo al repositorio que vuelva a guardar al TheActualUser. (que se
            // actualice)
            return this.theUserRepository.save(theActualUser);
        } else {
            // Si no encuentra usuario para actualizar devuelve nulo
            return null;
        }
    }

    // Método DELETE
    // El response status indica que cuando termine no devolverá nada en este caso.
    @ResponseStatus(HttpStatus.NO_CONTENT)
    // Elegimos el método DELETE que identificará al usuario con su ID
    @DeleteMapping("{id}")
    // public void (no devolverá nada). destroy puede ser cualquier nombre. Leemos
    // la variable que viene en la ruta, que es el identificador.
    public void destroy(@PathVariable String id) {
        User theUser = this.theUserRepository
                .findById(id)
                .orElse(null);
        if (theUser != null) {
            // Si el usaurio existe, le decimos al repositorio que elimine el usuario, y
            // será eliminado de la base de datos.
            this.theUserRepository.delete(theUser);
        }
    }

    // CLASE 05/09/2023 (6)
    // Creación dle endpoint para asociarle un rol a un usuario (match)

    // Lo hacemos con un PUT
    // Lo que está en llaves es pq es un valor que peude cambiar que ponemos.
    @PutMapping("{user_id}/role/{role_id}")
    // Necesito 2 variables que vienen en el path
    public User matchUserRole(@PathVariable String user_id,
                              @PathVariable String role_id) {
        // Necesito saber que el usuario y el rol existen
        User theActualUser=this.theUserRepository.findById(user_id).orElse(null);
        Role theActualRole=this.theRoleRepository.findById(role_id).orElse(null);

        // Si existen
        if(theActualUser != null && theActualRole != null){
            // Le agrego el rol al objeto de users
            theActualUser.setRole(theActualRole);
            // Guardo en la base de datos
            return this.theUserRepository.save(theActualUser);
        } else {
            return null;
        }
    }

    // Desasociar  (unmatch)
    // Sería un PUT pq vamos a actualizar esta asociación.
    // identificamos el usuario y el rol que le vamos a quitar.
    @PutMapping("{user_id}/role")
    public User unMatchUserRole(@PathVariable String user_id) {
        // COmprobamos que exista
        User theActualUser=this.theUserRepository.findById(user_id).orElse(null);
        // Si existe
        if(theActualUser != null){
            // Le ponemos el rol en null
            theActualUser.setRole(null);
            // Guardamos en la base de datos.
            return this.theUserRepository.save(theActualUser);
        } else {
            return null;
        }
    }

}
