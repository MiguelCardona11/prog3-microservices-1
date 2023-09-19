package com.mssecurity.mssecurity.Configurations;

import com.mssecurity.mssecurity.Interceptors.SecurityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
// IMPORTANTE PONER EL DECORADOR
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Se debe crear la dependencia con autowired, no podemos crearla directamente nosotros on un new SecurityInterceptor() ya que no se inyectarían las dependencias.
    // autowired significa que tiene que arrancar por si solo.
    @Autowired
    private SecurityInterceptor securityInterceptor;

    @Override
    // usamos el mpétodo de esta interfaz "addInterceptors"
    // No devuelve nada. creo un registro de interceptor.
    public void addInterceptors(InterceptorRegistry registry) {
        // Este metodo es para activar un interceptor cada vez que pase una ruta que lo tenga permitido (nosotros lod ecidimos)
        // el "securityInterceptor" hace referencia al interceptor que creamos
        registry.addInterceptor(securityInterceptor)
                // Necesito que ese interceptor se active cuando las rutas inicien con eso
                // en este caso, elegimos todas, para cuandoa cceda a cualquiera, acceda al interceptor
                .addPathPatterns("/api/**")
                // pero excluya estas, que se podrán acceder de forma pública
                // exceptuamos que vaya al interceptor cuando vaya al login, ya que en este momento no tendría el token
                /* t0do lo que se api debe estar protegido */
                .excludePathPatterns("/api/public/**");
                // resumen: protega todas las rutas excepto las de security
                // TENER ENCUENTA QUE SE CAMBIO LAS RUTAS DE LOS CONTROLLERS PARA HACER ESTO.
    }
}
