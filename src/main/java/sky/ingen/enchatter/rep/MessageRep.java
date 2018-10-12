package sky.ingen.enchatter.rep;

import org.springframework.data.jpa.repository.JpaRepository;
import sky.ingen.enchatter.domain.Message;

import java.util.List;

public interface MessageRep extends JpaRepository<Message, Long> {

    List<Message> findAllByOrderByCreationTimeAsc();

    List<Message> findByAuthorIdOrderByCreationTimeAsc(long id);
}
