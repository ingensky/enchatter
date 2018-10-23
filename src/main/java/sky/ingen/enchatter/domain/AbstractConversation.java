package sky.ingen.enchatter.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@EqualsAndHashCode(of = "id")
@Data
@NoArgsConstructor
abstract class AbstractConversation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
}
