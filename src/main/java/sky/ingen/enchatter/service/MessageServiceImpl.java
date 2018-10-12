package sky.ingen.enchatter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sky.ingen.enchatter.domain.Message;
import sky.ingen.enchatter.rep.MessageRep;
import sky.ingen.enchatter.util.exception.NotFoundException;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRep messageRep;

    @Override
    public Message create(Message message) {
        return messageRep.save(message);
    }

    @Override
    public void delete(long id) throws NotFoundException {
        messageRep.deleteById(id);
    }

    @Override
    public Message get(long id) throws NotFoundException {
        return messageRep.getOne(id);
    }

    @Override
    public void update(Message message) {
        messageRep.save(message);
    }

    @Override
    public List<Message> getAll() {
        return messageRep.findAllByOrderByCreationTimeAsc();
    }

    @Override
    public List<Message> getAllFromUser(long userId) {
        return messageRep.findByAuthorIdOrderByCreationTimeAsc(userId);
    }
}
