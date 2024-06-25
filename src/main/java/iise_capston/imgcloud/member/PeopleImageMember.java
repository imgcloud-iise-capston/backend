package iise_capston.imgcloud.member;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "PeopleImage")
public class PeopleImageMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long peopleId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private OauthMember userPeopleId;

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
    private double x;

    @Column
    private double y;

    @Column
    private double width;

    @Column
    private double height;

    @Column
    private String fileType;
}
