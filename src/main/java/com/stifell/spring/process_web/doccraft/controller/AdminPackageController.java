package com.stifell.spring.process_web.doccraft.controller;

import com.stifell.spring.process_web.doccraft.entity.TemplatePackage;
import com.stifell.spring.process_web.doccraft.exception.ResourceNotFoundException;
import com.stifell.spring.process_web.doccraft.service.TemplatePackageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

/**
 * @author stifell on 05.08.2025
 */
@Controller
@RequestMapping("/admin/packages")
public class AdminPackageController {
    private final TemplatePackageService packageService;

    @Autowired
    public AdminPackageController(TemplatePackageService packageService) {
        this.packageService = packageService;
    }

    @GetMapping
    public String listPackages(HttpServletRequest request, Model model) {
        model.addAttribute("currentURI", request.getRequestURI());
        model.addAttribute("packages", packageService.getAllPackages());
        return "admin/packages";
    }

    @GetMapping("/new")
    public String showCreateForm(HttpServletRequest request, Model model) {
        model.addAttribute("currentURI", request.getRequestURI());
        model.addAttribute("package", new TemplatePackage());
        return "admin/create-package";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, HttpServletRequest request, Model model) {
        model.addAttribute("currentURI", request.getRequestURI());
        TemplatePackage templatePackage = packageService.getPackageById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пакет не найден"));

        model.addAttribute("package", templatePackage);
        return "admin/create-package";
    }

    @PostMapping("/create")
    public String createPackage(@RequestParam("name") String name,
                                @RequestParam(value = "description", required = false) String description,
                                @RequestParam("files") MultipartFile[] files,
                                Principal principal,
                                RedirectAttributes redirectAttributes) {
        try {
            packageService.createPackage(name, description, files, principal.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Пакет успешно создан");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Ошибка создания пакета: " + e.getMessage());
        }
        return "redirect:/admin/packages";
    }

    @PostMapping("/update/{id}")
    public String updatePackage(@PathVariable Long id,
                                @RequestParam("name") String name,
                                @RequestParam(value = "description", required = false) String description,
                                @RequestParam(value = "newFiles", required = false) MultipartFile[] newFiles,
                                Principal principal,
                                RedirectAttributes redirectAttributes) {
        try {
            packageService.updatePackage(id, name, description, newFiles, principal.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Пакет успешно обновлен");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Ошибка обновления пакета: " + e.getMessage());
        }
        return "redirect:/admin/packages";
    }

    @PostMapping("/delete/{id}")
    public String deletePackage(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            packageService.deletePackage(id);
            redirectAttributes.addFlashAttribute("successMessage", "Пакет успешно удален");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Ошибка удаления пакета: " + e.getMessage());
        }
        return "redirect:/admin/packages";
    }

    @PostMapping("/delete-file/{id}")
    public String deletePackageFile(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Long packageId = packageService.deleteFile(id);
            redirectAttributes.addFlashAttribute("successMessage", "Файл успешно удален");
            return "redirect:/admin/packages/edit/" + packageId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка удаления файла: " + e.getMessage());
            return "redirect:/admin/packages";
        }
    }
}