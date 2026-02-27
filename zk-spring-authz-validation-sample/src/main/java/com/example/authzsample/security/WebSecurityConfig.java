package com.example.authzsample.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class WebSecurityConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails alice = User.withUsername("alice")
                .password("{noop}password")
                .roles("APP_USER")
                .build();
        UserDetails bob = User.withUsername("bob")
                .password("{noop}password")
                .roles("APP_USER")
                .build();
        return new InMemoryUserDetailsManager(alice, bob);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/login", "/error").permitAll()

                // Allow logout endpoint access (actual logout still requires authentication)
                .requestMatchers("/logout").permitAll()

                .requestMatchers("/zkau/**").authenticated()
                .requestMatchers(new AntPathRequestMatcher("/**/*.zul")).authenticated()
                .requestMatchers("/api/**").authenticated()
                .anyRequest().denyAll()
        );

        http.formLogin(form -> form
                .defaultSuccessUrl("/zul/dashboard.zul", true)
                .permitAll()
        );

        // IMPORTANT: dashboard.zul uses <a href="/logout">Logout</a> (GET).
        // Spring Security logout is POST-only by default when CSRF is enabled,
        // so we explicitly allow GET /logout to perform logout for this sample.
        http.logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
        );

        http.httpBasic(b -> {});

        http.exceptionHandling(e -> e
                .defaultAuthenticationEntryPointFor(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                        new AntPathRequestMatcher("/api/**")
                )
        );

        // Demo only: ignore CSRF for H2 + ZK AU + (now) GET logout usage.
        http.csrf(csrf -> csrf.ignoringRequestMatchers(
                new AntPathRequestMatcher("/h2-console/**"),
                new AntPathRequestMatcher("/zkau/**"),
                new AntPathRequestMatcher("/logout") // <--- add this for demo
        ));

        http.headers(h -> h.frameOptions(f -> f.sameOrigin()));

        return http.build();
    }
}