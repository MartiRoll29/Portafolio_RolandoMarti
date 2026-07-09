package pruabTech.Rolando;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    public static final String[] PUBLIC_URLS = {
        "/", "/index", "/consultas/**", "/registro/**",
        "/ejemplo", "/multimedia", "/iframes",
        "/css/**", "/js/**", "/webjars/**", "/login", "/acceso_denegado"
    };

    public static final String[] ADMIN_URLS = {
        "/producto/guardar", "/producto/eliminar", "/producto/modificar/**",
        "/categoria/guardar", "/categoria/eliminar", "/categoria/modificar/**"
    };

    public static final String[] ADMIN_OR_VENDEDOR_URLS = {
        "/producto/listado", "/categoria/listado"
    };

    public static final String[] USUARIO_URLS = {
        "/facturar/carrito"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> request
                .requestMatchers(PUBLIC_URLS).permitAll()
                .requestMatchers(ADMIN_URLS).hasRole("ADMIN")
                .requestMatchers(ADMIN_OR_VENDEDOR_URLS).hasAnyRole("ADMIN", "VENDEDOR")
                .requestMatchers(USUARIO_URLS).hasRole("USUARIO")
                .anyRequest().authenticated()
        ).formLogin(form -> form // Configuración de formulario de login
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
        ).logout(logout -> logout // Configuración de logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
        ).exceptionHandling(exceptions -> exceptions // Manejo de excepciones
                .accessDeniedPage("/acceso_denegado")
        ).sessionManagement(session -> session // Configuración de sesiones
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
        );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //Este método será reemplazado en una semana futura por una fuente persistente (base de datos)
    @Bean
    public UserDetailsService users(PasswordEncoder passwordEncoder) {
        UserDetails admin = User.builder()
                .username("juan")
                .password(passwordEncoder.encode("123"))
                .roles("ADMIN")
                .build();

        UserDetails vendedor = User.builder()
                .username("rebeca")
                .password(passwordEncoder.encode("456"))
                .roles("VENDEDOR")
                .build();

        UserDetails usuario = User.builder()
                .username("pedro")
                .password(passwordEncoder.encode("789"))
                .roles("USUARIO")
                .build();

        return new InMemoryUserDetailsManager(admin, vendedor, usuario);
    }
}
