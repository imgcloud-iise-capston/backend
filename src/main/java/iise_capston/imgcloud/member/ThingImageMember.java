package iise_capston.imgcloud.member;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Data
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

    @Column
    private String imageTitle;

    @Column
    private String coordinate;
}
