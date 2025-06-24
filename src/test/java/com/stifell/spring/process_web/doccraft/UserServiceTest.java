package com.stifell.spring.process_web.doccraft;

import com.stifell.spring.process_web.doccraft.entity.Role;
import com.stifell.spring.process_web.doccraft.entity.User;
import com.stifell.spring.process_web.doccraft.repository.RoleRepository;
import com.stifell.spring.process_web.doccraft.repository.UserRepository;
import com.stifell.spring.process_web.doccraft.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author stifell on 25.06.2025
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void saveUser_WithNewUser_ShouldSaveNewUser() {
        User user = new User();
        user.setUsername("newuser");
        user.setPassword("password");
        user.setPasswordConfirm("password");

        when(userRepository.findByUsername("newuser")).thenReturn(null);
        when(bCryptPasswordEncoder.encode("password")).thenReturn("encoded");
//        when(roleRepository.findById(2L)).thenReturn(Optional.of(new Role(2L, "ROLE_USER")));

        boolean result = userService.saveUser(user);

        assertTrue(result);
        assertEquals("newuser", user.getUsername());
        assertEquals("encoded", user.getPassword());
        assertTrue(user.isEnabled());
        verify(userRepository).save(user);
    }

    @Test
    void toggleUserStatus_ShouldChangeEnabled() {
        User user = new User();
        user.setId(1L);
        user.setEnabled(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.toggleUserStatus(1L);

        assertFalse(user.isEnabled());
        verify(userRepository).save(user);
    }
}
