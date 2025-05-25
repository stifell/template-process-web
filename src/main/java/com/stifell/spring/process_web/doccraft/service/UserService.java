package com.stifell.spring.process_web.doccraft.service;

import com.stifell.spring.process_web.doccraft.repository.RoleRepository;
import com.stifell.spring.process_web.doccraft.repository.UserRepository;
import com.stifell.spring.process_web.doccraft.entity.Role;
import com.stifell.spring.process_web.doccraft.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author stifell on 30.04.2025
 */
@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User" + username + " not found");
        }
        return user;
    }

    public User findByUserId(Long userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        return userFromDb.orElse(new User());
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean saveUser(User user) {
        User userFromDb = userRepository.findByUsername(user.getUsername());

        if (userFromDb != null) {
            return false;
        }

        user.setRoles(Collections.singleton(new Role(2L, "ROLE_USER")));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        userRepository.save(user);
        return true;
    }

    public void updateUser(User updateUser, String newPassword) {
        User existingUser = userRepository.findById(updateUser.getId()).orElseThrow(
                () -> new UsernameNotFoundException("User" + updateUser.getId() + " not found"));

        existingUser.setUsername(updateUser.getUsername());
        if (newPassword != null && !newPassword.isEmpty()) {
            existingUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
        }
        existingUser.setRoles(updateUser.getRoles());
        userRepository.save(existingUser);
    }

    public void deleteUser(Long userId) {
        User existingUser = userRepository.findById(userId).orElseThrow(
                () -> new UsernameNotFoundException("User" + userId + " not found"));

        existingUser.getRoles().clear();
        userRepository.save(existingUser);
        userRepository.delete(existingUser);
    }

    public void toggleUserStatus(Long userId) {
        User existingUser = userRepository.findById(userId).orElseThrow(
                () -> new UsernameNotFoundException("User" + userId + " not found")
        );
        existingUser.setEnabled(!existingUser.isEnabled());
        userRepository.save(existingUser);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
