package iise_capston.imgcloud.member;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;

@Entity
@RequiredArgsConstructor
@Table(name = "Brisque",uniqueConstraints = {@UniqueConstraint(name = "brisque_unique",columnNames = {"brisque_id"})})
public class BrisqueMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long brisqueId;

    @OneToOne
    @JoinColumn(name = "peopleId", nullable = false)
    private PeopleImageMember peopleId;

    @OneToOne
    @JoinColumn(name = "thingId", nullable = false)
    private ThingImageMember thingId;

    @Column
    private String graph;

    @Column
    private String distortion;

    @Column
    private double brisqueScore;


}
