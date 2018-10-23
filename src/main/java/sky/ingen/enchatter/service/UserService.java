package sky.ingen.enchatter.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import sky.ingen.enchatter.domain.User;
import sky.ingen.enchatter.util.exception.NotFoundException;

import java.util.List;

public interface UserService extends UserDetailsService {

    User create(User user);

    void delete(long id) throws NotFoundException;

    User get(long id) throws NotFoundException;

    void update(User user);

    List<User> getAll();

    User getByUsername(String username);
}
