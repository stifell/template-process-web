package com.stifell.spring.process_web.doccraft.controller;

import com.stifell.spring.process_web.doccraft.entity.User;
import com.stifell.spring.process_web.doccraft.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author stifell on 26.05.2025
 */
@Controller
@RequestMapping("/admin")
public class AdminEditUsersController {
    @Autowired
    private UserService userService;

    @GetMapping("/edit_users")
    public String getAllUsers(HttpServletRequest request, Model model) {
        model.addAttribute("currentURI", request.getRequestURI());
        model.addAttribute("users", userService.getAllUsers());
        return "admin/edit-users";
    }

    @GetMapping("/edit_user/{id}")
    public String getEditUser(@PathVariable Long id, Model model) {
        model.addAttribute("currentURI", "/admin/edit_users");

        User user = userService.findByUserId(id);
        model.addAttribute("user", user);
        model.addAttribute("allRoles", userService.getAllRoles());
        return "admin/edit-user-form";
    }

    @PostMapping("/update_user")
    public String updateUser(@ModelAttribute User updatedUser,
                             @RequestParam(value = "newPassword", required = false) String newPassword) {
        userService.updateUser(updatedUser, newPassword);
        return "redirect:/admin/edit_users";
    }

    @PostMapping("/delete_user/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/edit_users";
    }

    @PostMapping("/toggle_user/{id}")
    public String toggleUserStatus(@PathVariable Long id) {
        userService.toggleUserStatus(id);
        return "redirect:/admin/edit_users";
    }
}
