package sky.ingen.enchatter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import sky.ingen.enchatter.domain.Dialog;
import sky.ingen.enchatter.domain.Message;
import sky.ingen.enchatter.domain.User;
import sky.ingen.enchatter.rep.DialogRep;
import sky.ingen.enchatter.rep.MessageRep;
import sky.ingen.enchatter.rep.UserRep;
import sky.ingen.enchatter.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MessageService {

    private final MessageRep messageRep;

    private final DialogRep dialogRep;

    private final UserRep userRep;

    @Autowired
    public MessageService(MessageRep messageRep, DialogRep dialogRep, UserRep userRep) {
        this.messageRep = messageRep;
        this.dialogRep = dialogRep;
        this.userRep = userRep;
    }


    public Message create(Message message) {
        return messageRep.save(message);
    }


    public void delete(long id) throws NotFoundException {
        messageRep.deleteById(id);
    }


    public Message get(long id) throws NotFoundException {
        return messageRep.getOne(id);
    }


    public void update(Message message) {
        messageRep.save(message);
    }


    public List<Message> getAll() {
        return messageRep.findAllByOrderByCreationTimeAsc();
    }


    public List<Message> getAllFromUser(long userId) {
        return messageRep.findByAuthorIdOrderByCreationTimeAsc(userId);
    }


    public List<Message> allDialogMessages(Dialog dialog) {
        return messageRep.findByDialogOrderByCreationTime(dialog);
    }

    public Dialog getDialogForUser(@RequestParam(value = "p", required = false) String username, @AuthenticationPrincipal User authUser, List<Dialog> allForPrincipal) {
        User interlocutor = Optional.ofNullable(userRep.findByUsername(username))
                .orElseThrow(() ->
                        new NotFoundException(String.format("User %s not found", username)));
        Dialog conversation = getDialog(authUser, interlocutor, allForPrincipal);
        if (!allForPrincipal.contains(conversation)) {
            allForPrincipal.add(conversation);
        }
        return conversation;
    }

    private Dialog getDialog(User authUser, User interlocutor, List<Dialog> allForPrincipal) {
        return allForPrincipal.stream().filter(dialog ->
                (dialog.getInterlocutorOne().getId().equals(interlocutor.getId())) ||
                        (dialog.getInterlocutorTwo().getId().equals(interlocutor.getId()))
        ).findFirst()
                .orElseGet(() -> createNewDialog(authUser, interlocutor, null));
    }

    @Transactional
    public Dialog createNewDialog(User authUser, User interlocutor, Message greeting) {
        Dialog dialog = Dialog.builder()
                .interlocutorOne(authUser)
                .interlocutorTwo(interlocutor)
                .lastUpdate(LocalDateTime.now())
                .build();
        if (greeting != null && greeting.getText()!=null && !greeting.getText().isEmpty()) {
            greeting.setAuthor(authUser);
            greeting.setCreationTime(LocalDateTime.now());
            greeting.setDialog(dialog);
            create(greeting);
            dialog.setMessages(Collections.singleton(greeting));
        }
        return dialogRep.save(dialog);
    }

}
