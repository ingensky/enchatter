package sky.ingen.enchatter.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import sky.ingen.enchatter.domain.util.View;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

//https://stackoverflow.com/a/4591439/7667017
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@EqualsAndHashCode(of = "id")
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(View.Id.class)
    private Long id;

    @NotBlank(message = "Please enter some message")
    @Size(min = 2, max = 2048)
    @JsonView(View.Main.class)
    private String text;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd MMM HH:mm:ss")
    @JsonView(View.Body.class)
    private LocalDateTime creationTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonView(View.Main.class)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dialog_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Dialog dialog;

    public Message() {
    }

    public Message(@NotBlank(message = "Please enter some message") @Size(min = 2, max = 10000) String text) {
        this.text = text;
    }
}
