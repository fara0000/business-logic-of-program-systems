package backend.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Pin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * photo's service information
     */

    @Column(name = "originalFileName")
    private String originalFileName;

    @Column(name = "size")
    private Long size;

    @Column(name = "contentType")
    private String contentType;

    @Lob
    @Column(name = "bytesArray")
    private byte[] bytes;

    /**
     * pin's basic information
     */

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "alt_text")
    private String alt_text;

    @Column(name = "link")
    private String link;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private User user;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private Board board;
}
