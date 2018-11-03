package sky.ingen.enchatter.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sky.ingen.enchatter.domain.Dialog;
import sky.ingen.enchatter.domain.Message;
import sky.ingen.enchatter.domain.User;
import sky.ingen.enchatter.domain.util.View;
import sky.ingen.enchatter.rep.DialogRep;
import sky.ingen.enchatter.service.MessageService;
import sky.ingen.enchatter.service.UserService;
import sky.ingen.enchatter.util.exception.NotFoundException;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
            User interlocutor = Optional.ofNullable(userService.getByUsername(username))
                    .orElseThrow(() ->
                            new NotFoundException(String.format("User %s not found", username)));
            Dialog conversation = getDialog(authUser, interlocutor, allForPrincipal);
            if (!allForPrincipal.contains(conversation)) {
                allForPrincipal.add(conversation);
            }
            model.addAttribute("messages", messageService.allDialogMessages(conversation));
            model.addAttribute("interlocutor", username);
            model.addAttribute("dialogId", conversation.getId());
        } else {
            model.addAttribute("messages", Collections.EMPTY_LIST);
        }
        model.addAttribute("dialogs", allForPrincipal);
        model.addAttribute("message", new Message());
        log.debug("get dialog page {} <-> {}", authUser.getUsername(), username);
        return "chatter";


    }

    private Dialog getDialog(User authUser, User interlocutor, List<Dialog> allForPrincipal) {
        return allForPrincipal.stream().filter(dialog ->
                (dialog.getInterlocutorOne().getId().equals(interlocutor.getId())) ||
                        (dialog.getInterlocutorTwo().getId().equals(interlocutor.getId()))
        ).findFirst()
                .orElseGet(() -> createNewDialog(authUser, interlocutor));
    }

    private Dialog createNewDialog(User one, User two) {
        Dialog dialog = Dialog.builder()
                .interlocutorOne(one)
                .interlocutorTwo(two)
                .lastUpdate(LocalDateTime.now())
                .build();
        return dialogRep.save(dialog);
    }

    @MessageMapping("/dialog/{id}")
    @SendTo("/topic/activity/{id}")
    @JsonView(View.Body.class)
    @Transactional
    public Message sendMessageWS(
            Principal author,
            @DestinationVariable("id") Long dialogId,
            @Payload Message message
    ) {
        Dialog dialog = dialogRep.getById(dialogId);
        dialog.setLastUpdate(LocalDateTime.now());
        message.setDialog(dialog);
//        https://stackoverflow.com/a/46248706/7667017
        User principal = (User) ((Authentication) author).getPrincipal();
        message.setAuthor(principal);
        message.setCreationTime(LocalDateTime.now());
        log.debug("ws message {} to user with id {}", message.getText(), message.getAuthor().getId());
        return messageService.create(message);
    }
}
