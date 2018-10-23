package sky.ingen.enchatter.rep;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sky.ingen.enchatter.domain.Dialog;
import sky.ingen.enchatter.domain.User;

import java.util.List;

public interface DialogRep extends JpaRepository<Dialog, Long> {

    @Query("SELECT d from Dialog d where d.interlocutorOne = ?1 OR d.interlocutorTwo = ?1 ORDER BY d.lastUpdate")
    List<Dialog> getAllForPrincipal(User authUser);

    @Query("SELECT d from Dialog d where (d.interlocutorOne = ?1 AND d.interlocutorTwo = ?2) OR " +
            "(d.interlocutorOne = ?2 AND d.interlocutorTwo = ?1)")
    Dialog findDialog(User one, User two);
}
