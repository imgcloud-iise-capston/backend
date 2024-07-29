package iise_capston.imgcloud.member;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class MetadataMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long metaDataId;

    @OneToOne
    @JoinColumn(name = "peopleId")
    private PeopleImageMember peopleId;

    @OneToOne
    @JoinColumn(name = "thingId")
    private ThingImageMember thingId;

    @Column
    private int iso;

    @Column
    private String whiteBalance;

    @Column
    private double fStop;

    @Column
    private int exposureTime;

    @Column
    private String gpsLatitude;

    @Column
    private String gpsLongitude;

    @Column
    private String realResolution;

    @Column
    private String resolution;

    @Column
    private String size;

    @Column
    private int metaScore;

    @Column(nullable = false)
    private int pixelRow;
    @Column(nullable = false)
    private int pixelColumn;
}
