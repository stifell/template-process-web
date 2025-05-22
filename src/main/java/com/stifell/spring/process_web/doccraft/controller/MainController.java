package com.stifell.spring.process_web.doccraft.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author stifell on 28.04.2025
 */

@Controller
public class MainController {

    @GetMapping("/")
    public String getHomePage(HttpServletRequest request, Model model) {
        model.addAttribute("currentURI", request.getRequestURI());
        return "home-page";
    }

    @GetMapping("/upload")
    public String getUploadPage(HttpServletRequest request, Model model) {
        model.addAttribute("currentURI", request.getRequestURI());
        return "upload-page";
    }
}
