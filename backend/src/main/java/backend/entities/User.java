package backend.entities;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(min = 10, max = 50)
    @NotNull
    @NotEmpty
    @Column(name = "email", unique = true)
    private String email;

    @Size(min = 4)
    @NotNull
    @NotEmpty
    @Column(name = "password", unique = true)
    private String password;

    @Column(name = "age")
    private String age;
}
