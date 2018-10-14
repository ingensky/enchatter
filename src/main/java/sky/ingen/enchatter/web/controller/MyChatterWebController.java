package sky.ingen.enchatter.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sky.ingen.enchatter.domain.Message;
import sky.ingen.enchatter.domain.User;
import sky.ingen.enchatter.service.MessageService;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/my_chatter")
public class MyChatterWebController {

    @Autowired
    private MessageService msgService;

    @GetMapping
    public String getMyChatPage(Model model) {
        model.addAttribute("messages", msgService.getAllFromUser(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()));
        model.addAttribute("newMessage", new Message());
        return "my_chatter";
    }
}