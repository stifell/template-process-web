package com.stifell.spring.process_web.doccraft.repository;

import com.stifell.spring.process_web.doccraft.entity.TagMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author stifell on 20.06.2025
 */
public interface TagMetadataRepository extends JpaRepository<TagMetadata, String> {
}
