package net.quazar.apigateway.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import net.quazar.apigateway.config.service.JwtService;
import net.quazar.apigateway.entity.ApiError;
import net.quazar.apigateway.exception.NotFoundException;
import net.quazar.apigateway.exception.jwt.InvalidTokenException;
import net.quazar.apigateway.exception.jwt.TokenRevokedException;
import net.quazar.apigateway.proxy.ResourceServerProxy;
import net.quazar.apigateway.proxy.resource.TokenResponse;
import net.quazar.apigateway.proxy.resource.UserResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final ResourceServerProxy resourceServerProxy;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String jwt = authHeader.substring("Bearer ".length());
        TokenResponse token;
        try {
            token = resourceServerProxy.getToken(jwt);
            if (!jwt.equals(token.token())) throw new InvalidTokenException("Invalid token", LocalDateTime.now().toString());
            if (token.revoked()) throw new TokenRevokedException("Token is revoked", LocalDateTime.now().toString());
        } catch (NotFoundException e) {
            filterChain.doFilter(request, response);
            return;
        } catch (InvalidTokenException | TokenRevokedException e) {
            ApiError error = ApiError.builder()
                    .status(401)
                    .timestamp(e.getTimestamp())
                    .message(e.getMessage())
                    .build();
            response.setStatus(error.getStatus());
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(mapper.writeValueAsString(error));
            return;
        }
        jwt = token.token();
        String username = jwtService.extractUsername(jwt);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserResponse user;
            try {
                user = resourceServerProxy.getUserByUsername(username);
            } catch (NotFoundException e) {
                ApiError error = ApiError.builder()
                        .status(404)
                        .timestamp(e.getTimestamp())
                        .message(e.getMessage())
                        .build();
                response.setStatus(error.getStatus());
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(mapper.writeValueAsString(error));
                return;
            }
            UserDetails details =
                    new User(username, "", resourceServerProxy.getUserAuthorities(user.id())
                            .stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList());
            if (jwtService.isTokenValid(jwt, details)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        details, null, details.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
