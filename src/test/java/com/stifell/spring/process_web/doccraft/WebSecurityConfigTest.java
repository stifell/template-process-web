package com.stifell.spring.process_web.doccraft;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author stifell on 24.06.2025
 */

@SpringBootTest
@AutoConfigureMockMvc
public class WebSecurityConfigTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithAnonymousUser
    void whenAccessPublic_thenOk() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
        mockMvc.perform(get("/login")).andExpect(status().isOk());
        mockMvc.perform(get("/registration")).andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void whenAccessProtectedWithoutAuth_thenOk() throws Exception {
        mockMvc.perform(get("/upload")).andExpect(status().is3xxRedirection());
        mockMvc.perform(get("/history")).andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(roles = "USER")
    void whenUserAccessUpload_thenOk() throws Exception {
        mockMvc.perform(get("/upload")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void whenAdminAccess_thenOk() throws Exception {
        mockMvc.perform(get("/admin/edit_users")).andExpect(status().isOk());
    }
}
