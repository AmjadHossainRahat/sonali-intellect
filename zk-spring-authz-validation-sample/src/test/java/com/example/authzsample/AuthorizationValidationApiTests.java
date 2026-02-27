package com.example.authzsample;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthorizationValidationApiTests {

    @Autowired
    MockMvc mvc;

    @Test
    void createPayment_requiresAuth() throws Exception {
        mvc.perform(post("/api/payments").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void alice_canCreatePayment() throws Exception {
        mvc.perform(post("/api/payments")
                        .with(SecurityMockMvcRequestPostProcessors.user("alice").roles("APP_USER"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("payment-created"));
    }

    @Test
    void bob_cannotCreatePayment_forbidden() throws Exception {
        mvc.perform(post("/api/payments")
                        .with(SecurityMockMvcRequestPostProcessors.user("bob").roles("APP_USER"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isForbidden());
    }
}
