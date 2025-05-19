package com.stifell.spring.process_web.doccraft.controller;

import com.stifell.spring.process_web.doccraft.entity.User;
import com.stifell.spring.process_web.doccraft.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author stifell on 19.05.2025
 */
@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registration-page";
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        // 1 Валидация полей (@Size @NotBlank в User)
        if (bindingResult.hasErrors()) {
            return "registration-page";
        }

        // 2 Проверка совпадения паролей
        if (!user.getPassword().equals(user.getPasswordConfirm())) {
            model.addAttribute("errorMessage", "Пароли не совпадают");
            return "registration-page";
        }

        // 3 Сохранение пользователя через сервис
        boolean created = userService.saveUser(user);
        if (!created) {
            model.addAttribute("errorMessage", "Пользователь с таким именем уже существует");
            return "registration-page";
        }

        // 4 Успешно, показываем сообщение и чистим форму
        model.addAttribute("successMessage", "Пользователь успешно зарегистрирован");
        model.addAttribute("user", new User());
        return "registration-page";
    }
}
