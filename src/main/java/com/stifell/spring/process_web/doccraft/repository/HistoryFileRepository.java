package com.stifell.spring.process_web.doccraft.repository;

import com.stifell.spring.process_web.doccraft.entity.HistoryFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author stifell on 13.08.2025
 */
public interface HistoryFileRepository extends JpaRepository<HistoryFile, Long> {
    List<HistoryFile> findByHistoryId(Long historyId);
}
