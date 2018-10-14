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
@RequestMapping("/chatter")
public class ChatterWebController {

    @Autowired
    private MessageService msgService;

    @GetMapping
    public String getChatPage(Model model) {
        model.addAttribute("messages", msgService.getAll());
        model.addAttribute("newMessage", new Message());
        return "chatter";
    }

    @PostMapping
    public String sendNewMessage(@ModelAttribute("newMessage") Message message) {
        if (StringUtils.hasText(message.getText())) {
            User author = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            message.setAuthor(author);
            message.setCreationTime(LocalDateTime.now());
            msgService.create(message);
        }
        return "redirect:/chatter";
    }
}
