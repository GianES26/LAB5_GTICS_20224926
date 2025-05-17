package com.example.lab5.config;

import com.example.lab5.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebSecurityConfig {

    final UsuarioRepository usuarioRepository;
    final DataSource dataSource;

    public WebSecurityConfig(DataSource dataSource, UsuarioRepository usuarioRepository) {
        this.dataSource = dataSource;
        this.usuarioRepository = usuarioRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            if (request.getUserPrincipal() != null) {
                Authentication authentication = (Authentication) request.getUserPrincipal();
                String rol = authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .findFirst()
                        .orElse("USER");

                Map<String, String> redirectMap = new HashMap<>();
                redirectMap.put("ADMIN", "/admin/usuarios");
                redirectMap.put("USER", "/usuario/juego");

                String redirectUrl = redirectMap.getOrDefault(rol, "/home");
                new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
            } else {
                new DefaultRedirectStrategy().sendRedirect(request, response, "/openLoginWindow");
            }
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers( "/openLoginWindow", "/submitLoginForm").permitAll()
                        .requestMatchers("/home/**","/admin", "/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/home/**","/usuario", "/usuario/**").hasAuthority("USER")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(accessDeniedHandler())
                )
                .formLogin(form -> form
                        .loginPage("/openLoginWindow")
                        .loginProcessingUrl("/submitLoginForm")
                        .successHandler((request, response, authentication) -> {
                            HttpSession session = request.getSession();
                            session.setAttribute("usuario", usuarioRepository.findByEmail(authentication.getName()));

                            DefaultSavedRequest defaultSavedRequest =
                                    (DefaultSavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");

                            if (defaultSavedRequest != null) {
                                String targetUrl = defaultSavedRequest.getRequestURL();
                                new DefaultRedirectStrategy().sendRedirect(request, response, targetUrl);
                            } else {
                                String rol = authentication.getAuthorities().stream()
                                        .map(GrantedAuthority::getAuthority)
                                        .findFirst()
                                        .orElse("USER");

                                Map<String, String> redirectMap = new HashMap<>();
                                redirectMap.put("ADMIN", "/admin/usuarios");
                                redirectMap.put("USER", "/usuario/juego");

                                String redirectUrl = redirectMap.getOrDefault(rol, "/home");
                                new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
                            }
                        })
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/openLoginWindow")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                )
                .sessionManagement(session -> session
                        .invalidSessionUrl("/openLoginWindow")
                        .sessionAuthenticationErrorUrl("/openLoginWindow")
                        .maximumSessions(1)
                        .expiredUrl("/openLoginWindow?expired=true")
                );

        return http.build();
    }

    @Bean
    public UserDetailsManager users(DataSource dataSource) {
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        String sqlAuth = """
            SELECT email, pwd, 
                   CASE WHEN activo = '1' THEN true ELSE false END AS enabled 
            FROM usuario 
            WHERE email = ?
            """;
        String sqlAuto = """
            SELECT u.email, r.nombre 
            FROM usuario u 
            INNER JOIN rol r ON u.idrol = r.idrol 
            WHERE u.email = ?
            """;
        users.setUsersByUsernameQuery(sqlAuth);
        users.setAuthoritiesByUsernameQuery(sqlAuto);
        return users;
    }
}