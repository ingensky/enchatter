package sky.ingen.enchatter.rep;

import org.springframework.data.jpa.repository.JpaRepository;
import sky.ingen.enchatter.domain.User;

public interface UserRep extends JpaRepository<User, Long> {
}
