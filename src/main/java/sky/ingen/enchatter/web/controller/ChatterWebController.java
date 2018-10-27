package sky.ingen.enchatter.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import sky.ingen.enchatter.domain.Dialog;
import sky.ingen.enchatter.domain.Message;
import sky.ingen.enchatter.domain.User;
import sky.ingen.enchatter.rep.DialogRep;
import sky.ingen.enchatter.service.MessageService;
import sky.ingen.enchatter.service.UserService;
import sky.ingen.enchatter.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/chatter")
@Slf4j
public class ChatterWebController {

    private final MessageService messageService;

    private final UserService userService;

    private final DialogRep dialogRep;

    @Autowired
    public ChatterWebController(MessageService msgService, UserService userService, DialogRep dialogRep) {
        this.messageService = msgService;
        this.userService = userService;
        this.dialogRep = dialogRep;
    }


    @GetMapping
    public String getDialogPage(
            @RequestParam(value = "p", required = false) String username,
            @AuthenticationPrincipal User authUser,
            Model model
    ) {
        List<Dialog> allForPrincipal = dialogRep.getAllForPrincipal(authUser);
        if (username != null) {
            User interlocutor = userService.getByUsername(username);
            Dialog conversation;
            try {
                conversation = getDialog(interlocutor, allForPrincipal);
            }catch(NotFoundException e){
                conversation = new Dialog();
                conversation.setInterlocutorOne(authUser);
                conversation.setInterlocutorTwo(interlocutor);
                conversation.setLastUpdate(LocalDateTime.now());
                dialogRep.save(conversation);
            }
            model.addAttribute("messages", messageService.allDialogMessages(conversation));
            model.addAttribute("interlocutor", username);
        } else {
            model.addAttribute("messages", Collections.EMPTY_LIST);
        }
        model.addAttribute("dialogs", allForPrincipal);
        model.addAttribute("message", new Message());
        log.debug("get dialog page {} <-> {}", authUser.getUsername(), username);
        return "chatter";


    }

    private Dialog getDialog(User interlocutor, List<Dialog> allForPrincipal) {
        return allForPrincipal.stream().filter(dialog ->
                (dialog.getInterlocutorOne().getId().equals(interlocutor.getId())) ||
                        (dialog.getInterlocutorTwo().getId().equals(interlocutor.getId()))
        ).findFirst().orElseThrow(NotFoundException::new);
    }

    @PostMapping
    public String sendMessage(
            @RequestParam(value = "p") String username,
            @ModelAttribute("message") Message message,
            @AuthenticationPrincipal User author) {
        if (StringUtils.hasText(message.getText())) {
            log.debug("insert message {}", message.getText());
            message.setDialog(dialogRep.findDialog(author, userService.getByUsername(username)));
            message.setAuthor(author);
            message.setCreationTime(LocalDateTime.now());
            messageService.create(message);
            log.debug("message got id = {}", message.getId());

        }
        return "redirect:/chatter?p="+username;
    }
}
