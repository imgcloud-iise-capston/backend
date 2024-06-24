package iise_capston.imgcloud.member;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "Brisque",uniqueConstraints = {@UniqueConstraint(name = "brisque_unique",columnNames = {"brisque_id"})})
public class BrisqueMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long brisqueId;

    @OneToOne
    @JoinColumn(name = "peopleId")
    private PeopleImageMember peopleId;

    @OneToOne
    @JoinColumn(name = "thingId")
    private ThingImageMember thingId;

    @Column
    private String graph;

    @Column
    private String distortion;

    @Column
    private double brisqueScore;


}
