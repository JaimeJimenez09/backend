package SalesianosLosBoscos.Backend.fashionBlogApi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true); // Permitir credenciales
        config.addAllowedOrigin("http://localhost:4200"); // Origen permitido
        config.addAllowedHeader("*"); // Permitir todos los encabezados
        config.addAllowedMethod("*"); // Permitir todos los métodos (GET, POST, etc.)
        config.setMaxAge(3600L); // Tiempo máximo en segundos que el resultado de una solicitud preflight puede ser almacenado en caché

        // Opcional: Exponer ciertos encabezados en la respuesta
        config.addExposedHeader("Authorization");
        config.addExposedHeader("Content-Type");

        source.registerCorsConfiguration("/api/v1/**", config);
        return new CorsFilter(source);
    }
}
