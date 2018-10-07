package sky.ingen.enchatter.service;

import sky.ingen.enchatter.domain.Message;
import sky.ingen.enchatter.util.exception.NotFoundException;

import java.util.List;

public interface MessageService {

    Message create(Message message);

    void delete(long id) throws NotFoundException;

    Message get(long id) throws NotFoundException;

    void update(Message message);

    List<Message> getAll();

    List<Message> getAllFromUser(long userId);
}
