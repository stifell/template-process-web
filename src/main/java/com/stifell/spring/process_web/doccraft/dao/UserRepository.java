package com.stifell.spring.process_web.doccraft.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.stifell.spring.process_web.doccraft.entity.User;

/**
 * @author stifell on 30.04.2025
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
