package Controllers;

import Models.User;
import Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Este decorador hace que nos conectemos a hacer pruebas de springboot desde la misma máquina (para que no d eproblemas de cross)
@CrossOrigin
// Este decorador permite usar los CRUD
@RestController
// Los metodos de esta clase se van a activar cuando el usuario ponga en la ruta
@RequestMapping("/users")

public class UsersController {
    // Necesito una referencia a userRepository
    @Autowired
    // Estamos haciendo una inyección de dependencias.
    // Significa que voy a depender de una interfaz para actuar.
    // El "theUserRepository" hace que aún si cambiamos la interfaz a la que le hacemos referencia (para heredar sus métodos) en el reposiroty de Mongo a MySQL por ejemplo, no afectará acá al controlador.
    private UserRepository theUserRepository;



    // Método GET
    // Se va a activar cuando el cliente use un GET
    @GetMapping("")
    // Listo todos los usuarios que están en la clase usuarios. (Al usar "User" estamos interactuando con el Modelo)
    public List<User> index(){
        // (Me estoy conectando con el repositorio, que es el que tiene el CRUD)
        // El repositorio es el que se comunica con la base de datos (que se crea sola en springboot), entonces nos retorna todos los usuarios de la BD con el método findAll que la interfaz heredó de Mongo.
        return this.theUserRepository.findAll();
    }


    //Método POST (store)
    // Respuesta para cuando funciona
    @ResponseStatus(HttpStatus.CREATED)
    // Se va a activar cuando el cliente use el POST
    @PostMapping

    // Voy a devolver un User, viene un parametro con un Generic (el va a fijarse en el pedazo "body" del JSON, y lo casteamos en el formato de la clase User)
    public User store(@RequestBody User newUser){
        // le digo al repositorio que me guarde el usuario, y este lo guarda en Mongo
        return this.theUserRepository.save(newUser);
        // Automáticamente lo retorna en formato JSON.
    }


    // Este metodo es para ver info de 1 solo usuario
    // Por lo tanto es una ampliación del método GET
    // Envío en la URL un identificador
    @GetMapping("{id}")
    //Leo el identificador el nombre que viene en la ruta.
    public User show(@PathVariable String id){
        // busco al usuario por su identificador
        User theUser=this.theUserRepository
                //Le pregunto al repositorio que si puede buscar a un usuario por su identificador
                .findById(id)
                // Si no lo encuentra, devuelva un null
                .orElse(null);
        // Retorne la información del usuario
        return theUser;
    }

    // Método para actualización (PUT)
    // Seleccionamos el metodo para PUT con el "@" que recibira el identificador
    @PutMapping("{id}")
    // Devolverá un usuario. Leemos el identificador que viene en la ruta con el @PathVariable (usuario antiguo). Y luego necesito la información del nuevo usuario, lo que va en el body
    public User update(@PathVariable String id,@RequestBody User theNewUser){
        // El usuario actual es el que esta en la base de datos (llego a esta con el Repository)
        User theActualUser=this.theUserRepository
                .findById(id)
                .orElse(null);
        // Si el usuario actual existe
        if (theActualUser!=null){
            // Le actualizo al Usuario actual todos los datos nuevos que se hayan puesto en el body
            theActualUser.setName(theNewUser.getName());
            theActualUser.setEmail(theNewUser.getEmail());
            theActualUser.setPassword(theNewUser.getPassword());
            // Le digo al repositorio que vuelva a guardar al TheActualUser. (que se actualice)
            return this.theUserRepository.save(theActualUser);
        }else{
            // Si no encuentra usuario para actualizar devuelve nulo
            return null;
        }
    }

    // Método DELETE
    // El response status indica que cuando termine no devolverá nada en este caso.
    @ResponseStatus(HttpStatus.NO_CONTENT)
    // Elegimos el método DELETE que identificará al usuario con su ID
    @DeleteMapping("{id}")
    // public void (no devolverá nada). destroy puede ser cualquier nombre. Leemos la variable que viene en la ruta, que es el identificador.
    public void destroy(@PathVariable String id){
        User theUser=this.theUserRepository
                .findById(id)
                .orElse(null);
        if (theUser!=null){
            // Si el usaurio existe, le decimos al repositorio que elimine el usuario, y será eliminado de la base de datos.
            this.theUserRepository.delete(theUser);
        }
    }

}
