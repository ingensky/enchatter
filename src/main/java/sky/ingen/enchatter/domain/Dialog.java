package sky.ingen.enchatter.domain;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"interlocutor_one_id", "interlocutor_two_id"}, name = "dialog_unique_interlocutors_idx")})
public class Dialog extends AbstractConversation {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interlocutor_one_id")
    private User interlocutorOne;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interlocutor_two_id")
    private User interlocutorTwo;

    @OneToMany(mappedBy = "dialog", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Message> messages;
}
