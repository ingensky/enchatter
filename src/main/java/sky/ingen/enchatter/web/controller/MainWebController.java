package sky.ingen.enchatter.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sky.ingen.enchatter.domain.Role;
import sky.ingen.enchatter.domain.User;
import sky.ingen.enchatter.service.UserService;

import java.util.Collections;

@Controller
@RequestMapping("/")
public class MainWebController {

    @Autowired
    private UserService service;

    @GetMapping
    public String getStartPage(Model model) {
        model.addAttribute("users", service.getAll());
        model.addAttribute("user", new User());
        return "home";
    }

    @PostMapping
    public String registerSubmit(
            @ModelAttribute("user") User user
            ) {
        user.setRoles(Collections.singleton(Role.USER));
        service.create(user);
        return "redirect:/login";
    }
}
