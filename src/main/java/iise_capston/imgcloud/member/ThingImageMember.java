package iise_capston.imgcloud.member;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "ThingImage")
public class ThingImageMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long thingId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private OauthMember userThingId;

    @Column
    private String imageKey;

    @Column
    private String imageUrl;

    @Column
    private String smallImageKey;

    @Column
    private String smallImageUrl;

//    @Column
//    private double totalScore;
//
//    @Column
//    private double metaScore;
//
//    @Column
//    private double brisqueScore;

    @Column
    private String imageTitle;

    @Column
    private String coordinate;
}
