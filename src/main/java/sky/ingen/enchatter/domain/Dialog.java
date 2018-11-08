package sky.ingen.enchatter.domain;


import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@ToString(exclude = {"messages"})
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime lastUpdate;
}
