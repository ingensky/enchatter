package sky.ingen.enchatter.rep;

import org.springframework.data.jpa.repository.JpaRepository;
import sky.ingen.enchatter.domain.Message;

public interface MessageRep extends JpaRepository<Message, Long> {
}
