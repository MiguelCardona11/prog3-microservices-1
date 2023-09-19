package com.mssecurity.mssecurity.Controllers;

import com.mssecurity.mssecurity.Models.User;
import com.mssecurity.mssecurity.Repositories.UserRepository;
import com.mssecurity.mssecurity.Services.EncryptionService;
import com.mssecurity.mssecurity.Services.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin
@RestController
// CLASE 14/09/2023 (9): cambiamos la ruta de ("security") a ("api/public/security")
// este controller es el único que queda con el "public" ya que este no pasará por el interceptor, ya que en el login aun no se tiene un token, ya que apenas se crea acá.
@RequestMapping("api/public/security")
public class SecurityController {

    // Necesitamos el repositorio de usuarios
     @Autowired
     private UserRepository theUserRepository;

     // Necesitamos lo de JWT

     @Autowired
     private JwtService jwtService;

     @Autowired
     private EncryptionService encryptionService;

     // Método login
     // CLASE 12/09/2023 (8) Implementación del login manual.
     // usamos el método POST
     // accedo a este método mediante la ruta /login
     @PostMapping("login")
     // el "final" es para poder usar la respuesta HTTP 401 que es el error por si se equivocan en el login
     // levantamos la excepción de errores con el throws IOException (para implementarlo en el else de mas adelante)
     public String login(@RequestBody User theUser, final HttpServletResponse response) throws IOException {
         String token = "";
         // Encontramos el usuario con el correo que manden.
         User actualUser = this.theUserRepository.getUserByEmail(theUser.getEmail());
         // verificamos que si exista
         // convierto la contraseña a SHA256 la contraseña del usuario actual
         // si la contraseña del de la base de datos es igual a la del usuario que ingresé
         if (actualUser!=null && actualUser.getPassword().equals(encryptionService.convertirSHA256(theUser.getPassword()))){
             // creamos el token
             // generamos el token, esto nos devuelve un string, que será la respuesta (o token mejor dicho)
             token = jwtService.generateToken(actualUser);
         } else {
             // manejar el problema (levantamos una excepción) para que no devuelva el token y mande un error 401 (unauthorized)
             response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
         }
         return token;
     }
     // Método logout
     // Método reset pass
}

