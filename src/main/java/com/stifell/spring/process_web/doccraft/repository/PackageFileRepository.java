package com.stifell.spring.process_web.doccraft.repository;

import com.stifell.spring.process_web.doccraft.entity.PackageFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author stifell on 05.08.2025
 */
public interface PackageFileRepository extends JpaRepository<PackageFile, Long> {
    List<PackageFile> findByTemplatePackageId(Long packageId);

    void deleteByTemplatePackageId(Long packageId);
}
