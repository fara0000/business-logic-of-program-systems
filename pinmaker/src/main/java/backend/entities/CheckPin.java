package backend.entities;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
public class CheckPin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JoinColumn(name = "pin_id")
    @OneToOne
    private Pin pin;

    @Column(name = "is_check")
    private boolean isCheck;
}
