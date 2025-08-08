package com.stifell.spring.process_web.doccraft.repository;

import com.stifell.spring.process_web.doccraft.entity.TemplatePackage;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author stifell on 05.08.2025
 */
public interface TemplatePackageRepository extends JpaRepository<TemplatePackage, Long> {
    @EntityGraph(attributePaths = {"files"})
    List<TemplatePackage> findAll(Sort sort);
}
