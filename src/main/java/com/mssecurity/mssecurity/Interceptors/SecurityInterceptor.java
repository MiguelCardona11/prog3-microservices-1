package com.mssecurity.mssecurity.Interceptors;

import com.mssecurity.mssecurity.Services.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

// Usamos la interfaz HandlerInterceptor, que nos obliga a usar métodos (eso hace una interfaz)
// Obligatoriamente tiene que usar el decorador Component
@Component
public class SecurityInterceptor implements HandlerInterceptor {
    // definimos una variable que es una constante. "final" es porque eso no se puede cambiar.
    // Defino el prefijo de algo, que cada vez que mande el token, debe ir acompañado de "Bearer "
    private static final String BEARER_PREFIX = "Bearer ";
    // hacemos sobreescritura del método (del que está en la interfaz que implementamos)

    // "importamos" el servicio jwtservices para poder usar el método que tiene para comprobar un token
    @Autowired
    private JwtService jwtService;
    @Override
    // request: es la carta, lo que viene del paquete, donde vienen los parámetros, el body, el Authorization (y dentro de este esta el token Bearer que probaremos (ver documento para prueba desde POSTMAN))
    // response: es con loq ue yo podría responder, en este caso puede ser un 401 (aunque se puede usar esto como un objeto)
    // handler:
    // devuelve true o false. (prehandle es porque entra la petición)
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean success = true;
        // Obtener lo que está en el encabezado de los parámetros de "Authorization"
        String authorizationHeader = request.getHeader("Authorization");

        // si existe y empieza con ("Bearer ") (significa que en la prueba desde POSTMAN metimos un Bearer Token
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            // obtenemos el token que viene con el bearer
            // le decimos que parta el string desde la longitiud del BEARER_PREFIX en adelante para quedar solo con el token
            String token = authorizationHeader.substring(BEARER_PREFIX.length());
            // Imprimimos el token
            System.out.println("Bearer Token: " + token);
            // valido que el token sea correcto (si true o false).
            success = jwtService.validateToken(token);
        } else {
            // si ni siquiera cumple el primer if, retorna falso
            success = false;
        }
        return success;
    }



}
