package sky.ingen.enchatter.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sky.ingen.enchatter.domain.Dialog;
import sky.ingen.enchatter.domain.Message;
import sky.ingen.enchatter.domain.Role;
import sky.ingen.enchatter.domain.User;
import sky.ingen.enchatter.rep.DialogRep;
import sky.ingen.enchatter.service.MessageService;
import sky.ingen.enchatter.service.UserService;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class MainWebController {

    private final UserService userService;
    private final DialogRep dialogRep;

    @Autowired
    public MainWebController(UserService userService, DialogRep dialogRep) {
        this.userService = userService;
        this.dialogRep = dialogRep;
    }

    @GetMapping
    public String getStartPage(Model model, @AuthenticationPrincipal User authUser) {
        // todo - delete unused stuff from this method
        model.addAttribute("users", userService.getAll());
        model.addAttribute("user", new User());
        Map<String, Long> interlocutorsWithDialogId = null;
        if (authUser != null) {
            interlocutorsWithDialogId = dialogRep.getAllForPrincipal(authUser).stream().collect(
                    Collectors.toMap(
                            dialog -> authUser.equals(dialog.getInterlocutorOne()) ?
                                    dialog.getInterlocutorTwo().getUsername() :
                                    dialog.getInterlocutorOne().getUsername(),
                            dialog -> dialog.getId()));
        }
        model.addAttribute("interlocutorsWithDialogId", interlocutorsWithDialogId);
        Message greeting = new Message();
        greeting.setText("Hi");
        model.addAttribute("greeting", greeting);
        return "home";
    }




    @PostMapping
    public String registerSubmit(
            @ModelAttribute("user") User user
            ) {
        user.setRoles(Collections.singleton(Role.USER));
        userService.create(user);
        return "redirect:/login";
    }
}
