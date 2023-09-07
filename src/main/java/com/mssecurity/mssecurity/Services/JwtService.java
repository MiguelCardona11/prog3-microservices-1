package com.mssecurity.mssecurity.Services;

import com.mssecurity.mssecurity.Models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
// Anotación de que es un servicio
@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret; // Esta es la clave secreta que se utiliza para firmar el
    //token. Debe mantenerse segura.
    @Value("${jwt.expiration}")
    private Long expiration; // Tiempo de expiración del token en milisegundos.
    // Creo la secret key
    private Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    // Metodo para crear el token
    public String generateToken(User theUser) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        // Esto es para guardar la información que queremos.
        Map<String, Object> claims = new HashMap<>();
        claims.put("_id", theUser.get_id());
        claims.put("name", theUser.getName());
        claims.put("email", theUser.getEmail());
        claims.put("role", theUser.getRole());

        // Crea un token de usuario
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(theUser.getName())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            // Verifica la expiración del token
            Date now = new Date();
            if (claimsJws.getBody().getExpiration().before(now)) {
                return false;
            }
            return true;
        } catch (SignatureException ex) {
            // La firma del token es inválida
            return false;
        } catch (Exception e) {
            // Otra excepción
            return false;
        }
    }
}
