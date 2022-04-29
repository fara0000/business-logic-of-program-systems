package adminTools.entities;

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
public class CheckBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JoinColumn(name = "board_id")
    @OneToOne()
    private Board board;

    @Column(name = "is_check")
    private boolean isCheck;

}
