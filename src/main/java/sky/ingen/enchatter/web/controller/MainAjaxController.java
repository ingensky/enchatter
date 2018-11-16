package sky.ingen.enchatter.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sky.ingen.enchatter.domain.Dialog;
import sky.ingen.enchatter.domain.Message;
import sky.ingen.enchatter.domain.User;
import sky.ingen.enchatter.domain.util.View;
import sky.ingen.enchatter.service.MessageService;

@RestController
@RequestMapping("/ajax")
@Slf4j
public class MainAjaxController {


    private final MessageService messageService;

    @Autowired
    public MainAjaxController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping(value = "/new_dialog/{user_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(View.Id.class)
    public Dialog newDialogSubmit(
            @PathVariable("user_id") User interlocutor,
            @AuthenticationPrincipal User authUser,
            @RequestBody Message greeting
    ) {
        log.debug("creating new dialog between {} and {}, with message: {}",
                interlocutor.getUsername(),
                authUser.getUsername(),
                greeting.getText());
        return messageService.createNewDialog(authUser, interlocutor, greeting);
    }

}
