package com.stifell.spring.process_web.doccraft.repository;

import com.stifell.spring.process_web.doccraft.entity.GenerationHistory;
import com.stifell.spring.process_web.doccraft.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author stifell on 13.08.2025
 */
public interface GenerationHistoryRepository extends JpaRepository<GenerationHistory, Long> {
    List<GenerationHistory> findByUserOrderByGenerationDateDesc(User user);
}
