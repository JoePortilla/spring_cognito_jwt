package co.cuencas.usersadmin.security;

import co.cuencas.usersadmin.security.jwt.JwtEntryPoint;
import co.cuencas.usersadmin.security.jwt.JwtProvider;
import co.cuencas.usersadmin.security.jwt.JwtTokenFilter;
import co.cuencas.usersadmin.security.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class MainSecurity {
    private final CorsFilter corsFilter;
    private final JwtEntryPoint jwtEntryPoint;
    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsService;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(corsFilter,
                             CorsFilter.class)
            .addFilterBefore(new JwtTokenFilter(jwtProvider, userDetailsService),
                             UsernamePasswordAuthenticationFilter.class)
            .csrf(AbstractHttpConfigurer::disable)
            // Indicate that i am in a stateless application. No need to handle the session
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(jwtEntryPoint))
            .authorizeHttpRequests((requests) -> requests
                                           // Make the login endpoint public
                                           .requestMatchers(HttpMethod.POST, "/auth/register", "/auth/login").permitAll()
                                           // The rest of the endpoints are protected via the authentication
                                           .anyRequest().authenticated()
                                  );
        return http.build();
    }
}
