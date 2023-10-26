package com.mssecurity.mssecurity.Services;

import com.mssecurity.mssecurity.Models.Permission;
import com.mssecurity.mssecurity.Models.Role;
import com.mssecurity.mssecurity.Models.RolePermission;
import com.mssecurity.mssecurity.Models.User;
import com.mssecurity.mssecurity.Repositories.PermissionRepository;
import com.mssecurity.mssecurity.Repositories.RolePermissionRepository;
import com.mssecurity.mssecurity.Repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidatorsService {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private PermissionRepository thePermissionRepository;
    @Autowired
    private UserRepository theUserRepository;
    @Autowired
    private RolePermissionRepository theRolePermissionRepository;
    private static final String BEARER_PREFIX = "Bearer ";
    // VER EXPLICACION DEL CODIGO DE INTERCEPTOR en el documento con las clases anteriores
    public boolean validationRolePermission(HttpServletRequest request,String url,String method){
        boolean success=false;
        // obtengo el usuario
        User theUser=this.getUser(request);
        if(theUser!=null){
            // verifico rol del usuario
            Role theRole=theUser.getRole();
            System.out.println("Antes URL "+url+" metodo "+method);
            url = url.replaceAll("[0-9a-fA-F]{24}|\\d+", "?");
            System.out.println("URL "+url+" metodo "+method);
            // busco un permiso dado la URL y el método
            Permission thePermission=this.thePermissionRepository.getPermission(url,method);
            if(theRole!=null && thePermission!=null){
                System.out.println("Rol "+theRole.getName()+ " Permission "+thePermission.getUrl());
                RolePermission theRolePermission=this.theRolePermissionRepository.getRolePermission(theRole.get_id(),thePermission.get_id());
                if (theRolePermission!=null){
                    success=true;
                }
            }else{
                success=false;
            }
        }
        return success;
    }
    // Creo una API que va a validar el token, el token siempre viene en el encabezado de la solicitud
    public User getUser(final HttpServletRequest request) {
        User theUser=null;
        String authorizationHeader = request.getHeader("Authorization");
        System.out.println("Header "+authorizationHeader);
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            String token = authorizationHeader.substring(BEARER_PREFIX.length());
            System.out.println("Bearer Token: " + token);
            User theUserFromToken=jwtService.getUserFromToken(token);
            if(theUserFromToken!=null) {
                theUser= this.theUserRepository.findById(theUserFromToken.get_id())
                        .orElse(null);
                theUser.setPassword("");
            }
        }
        // devuelve toda la información del usuario menos la contraseña
        return theUser;
    }
}