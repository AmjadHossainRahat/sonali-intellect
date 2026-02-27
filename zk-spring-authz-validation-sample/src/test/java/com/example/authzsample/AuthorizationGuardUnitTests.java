package com.example.authzsample;

import com.example.authzsample.zk.AuthorizationGuard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AuthorizationGuardUnitTests {

    @Autowired
    AuthorizationGuard guard;

    @AfterEach
    void cleanup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void unauthenticated_user_cannot() {
        assertThat(guard.can("PAYMENT", "CREATE")).isFalse();
    }

    @Test
    void alice_can_paymentCreate() {
        var auth = new UsernamePasswordAuthenticationToken(
                "alice", "N/A",
                List.of(new SimpleGrantedAuthority("ROLE_APP_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertThat(guard.can("PAYMENT", "CREATE")).isTrue();
    }

    @Test
    void bob_cannot_paymentCreate() {
        var auth = new UsernamePasswordAuthenticationToken(
                "bob", "N/A",
                List.of(new SimpleGrantedAuthority("ROLE_APP_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertThat(guard.can("PAYMENT", "CREATE")).isFalse();
    }
}
