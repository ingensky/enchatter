package sky.ingen.enchatter.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class UserInfo {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @MapsId
    private User user;

    private String CountryCode;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Byte communicationLevel;
}
