package com.stifell.spring.process_web.doccraft.service;

import com.stifell.spring.process_web.doccraft.entity.PackageFile;
import com.stifell.spring.process_web.doccraft.entity.TemplatePackage;
import com.stifell.spring.process_web.doccraft.entity.User;
import com.stifell.spring.process_web.doccraft.exception.ResourceNotFoundException;
import com.stifell.spring.process_web.doccraft.repository.PackageFileRepository;
import com.stifell.spring.process_web.doccraft.repository.TemplatePackageRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author stifell on 05.08.2025
 */
@Slf4j
@Service
@Transactional
public class TemplatePackageService {
    private static final Logger logger = LoggerFactory.getLogger(TemplatePackageService.class);
    private final TemplatePackageRepository packageRepository;
    private final PackageFileRepository fileRepository;
    private final UserService userService;

    @Autowired
    public TemplatePackageService(TemplatePackageRepository packageRepository,
                                  PackageFileRepository fileRepository,
                                  UserService userService) {
        this.packageRepository = packageRepository;
        this.fileRepository = fileRepository;
        this.userService = userService;
    }

    public List<TemplatePackage> getAllPackages() {
        return packageRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }


    public Optional<TemplatePackage> getPackageById(Long packageId) {
        return packageRepository.findById(packageId);
    }

    public TemplatePackage createPackage(String name, String description, MultipartFile[] files, String username) throws IOException {
        User user = userService.findByUsername(username);

        TemplatePackage templatePackage = new TemplatePackage();
        templatePackage.setName(name);
        templatePackage.setDescription(description);
        templatePackage.setCreatedBy(user);
        templatePackage.setCreatedAt(LocalDateTime.now());

        TemplatePackage savedPackage = packageRepository.save(templatePackage);
        addFilesToPackage(savedPackage, files);

        return savedPackage;
    }

    public TemplatePackage updatePackage(Long id, String name, String description,
                                         MultipartFile[] newFiles, String username) throws IOException {
        TemplatePackage templatePackage = packageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пакет не найден"));

        templatePackage.setName(name);
        templatePackage.setDescription(description);

        if (newFiles != null && newFiles.length > 0) {
            addFilesToPackage(templatePackage, newFiles);
        }

        return packageRepository.save(templatePackage);
    }

    public Long deletePackage(Long packageId) {
        fileRepository.deleteByTemplatePackageId(packageId);
        packageRepository.deleteById(packageId);
        return packageId;
    }

    private void addFilesToPackage(TemplatePackage templatePackage, MultipartFile[] files) throws IOException {
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                PackageFile packageFile = new PackageFile();
                packageFile.setFileName(file.getOriginalFilename());
                packageFile.setContent(file.getBytes());
                packageFile.setTemplatePackage(templatePackage);
                fileRepository.save(packageFile);
            }
        }
    }

    public Long deleteFile(Long fileId) {
        logger.info("Deleting file with ID: {}", fileId);
        PackageFile file = fileRepository.findById(fileId)
                .orElseThrow(() -> {
                    logger.error("File not found with ID: {}", fileId);
                    return new ResourceNotFoundException("Файл не найден");
                    });
        Long packageId = file.getTemplatePackage().getId();
        logger.info("Deleting file '{}' from package ID: {}", file.getFileName(), packageId);

        fileRepository.deleteById(fileId);

        logger.info("File deleted successfully");
        return packageId;
    }
}