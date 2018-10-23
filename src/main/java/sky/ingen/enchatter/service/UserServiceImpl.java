package sky.ingen.enchatter.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sky.ingen.enchatter.domain.User;
import sky.ingen.enchatter.rep.UserRep;
import sky.ingen.enchatter.util.exception.NotFoundException;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRep userRep;

    public UserServiceImpl(UserRep userRep) {
        this.userRep = userRep;
    }

    @Override
    public User create(User user) {
        return userRep.save(user);
    }

    @Override
    public void delete(long id) throws NotFoundException {
        userRep.deleteById(id);
    }

    @Override
    public User get(long id) throws NotFoundException {
        return userRep.getOne(id);
    }

    @Override
    public void update(User user) {
        userRep.save(user);
    }

    @Override
    public List<User> getAll() {
        return userRep.findAll();
    }

    @Override
    public User getByUsername(String username) {
        return userRep.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getByUsername(username);
    }
}
