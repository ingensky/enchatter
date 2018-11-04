package sky.ingen.enchatter.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sky.ingen.enchatter.domain.User;
import sky.ingen.enchatter.rep.UserRep;
import sky.ingen.enchatter.util.exception.NotFoundException;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private UserRep userRep;

    public UserService(UserRep userRep) {
        this.userRep = userRep;
    }


    public User create(User user) {
        return userRep.save(user);
    }


    public void delete(long id) throws NotFoundException {
        userRep.deleteById(id);
    }


    public User get(long id) throws NotFoundException {
        return userRep.getOne(id);
    }


    public void update(User user) {
        userRep.save(user);
    }


    public List<User> getAll() {
        return userRep.findAll();
    }


    public User getByUsername(String username) {
        return userRep.findByUsername(username);
    }


    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getByUsername(username);
    }
}
