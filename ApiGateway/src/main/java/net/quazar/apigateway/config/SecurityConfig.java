package net.quazar.apigateway.config;

import lombok.AllArgsConstructor;
import net.quazar.apigateway.config.filter.JwtAuthenticationFilter;
import net.quazar.apigateway.config.service.JwtService;
import net.quazar.apigateway.proxy.AuthorizationServerProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@AllArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity()
@Configuration
public class SecurityConfig {
    private final JwtService jwtService;
    private final AuthorizationServerProxy authorizationServerProxy;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeHttpRequests(c -> c.requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtService, authorizationServerProxy),
                        UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}