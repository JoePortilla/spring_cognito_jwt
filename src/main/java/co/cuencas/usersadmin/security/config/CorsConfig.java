package co.cuencas.usersadmin.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

/**
 * CORS Configuration
 */
@Configuration
@EnableWebMvc
public class CorsConfig {
    private static final Long MAX_AGE = 3600L;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        // Allows the backend to receive the headers which contain the authentication info.
        config.setAllowCredentials(true);
        // URL of the frontend
        config.setAllowedOrigins(List.of("http://localhost:4200/"));
        // Specify custom headers that must be available to client-side code in CORS requests.
        config.addExposedHeader("Message");
        // Headers that the application must accept
        config.setAllowedHeaders(List.of("Authorization", "Cache-control", "Content-Type"));
        // Methods that the application must accept
        config.setAllowedMethods(List.of("HEAD", "GET", "POST", "PUT", "DELETE"));
        // Time the CORS configuration is accepted
        config.setMaxAge(MAX_AGE);
        // Apply the configuration to all my routes
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);

    }

}
