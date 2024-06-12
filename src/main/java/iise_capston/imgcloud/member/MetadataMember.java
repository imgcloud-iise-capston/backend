package iise_capston.imgcloud.member;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;

@Entity
@RequiredArgsConstructor
public class MetadataMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long metaDataId;

    @OneToOne
    @JoinColumn(name = "peopleId", nullable = false)
    private PeopleImageMember peopleId;

    @OneToOne
    @JoinColumn(name = "thingId", nullable = false)
    private ThingImageMember thingId;

    @Column
    private int iso;

    @Column
    private int whiteBalance;

    @Column
    private String fStop;

    @Column
    private int exposureTime;

    @Column
    private String imageName;

    @Column
    private String gps;

    @Column
    private String size;

    @Column(nullable = false)
    private int pixelRow;
    @Column(nullable = false)
    private int pixelColumn;
}
