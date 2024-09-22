package iise_capston.imgcloud.member;

import iise_capston.imgcloud.oauth.OauthId;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="User",
uniqueConstraints = {@UniqueConstraint(name="user_unique",columnNames = {"email"}),
})
public class OauthMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Embedded
    private OauthId oauthId;
    @Column
    private String nickname;
    @Column
    private String profile;
    @Column
    private String email;

    @Column
    private boolean payment;

    @OneToMany(mappedBy = "userPeopleId")
    private List<PeopleImageMember> userPeople = new ArrayList<>();

    @OneToMany(mappedBy = "userThingId")
    private List<ThingImageMember> userThing = new ArrayList<>();

    public Long userId(){
        return userId;
    }
    public OauthId oauthId(){
        return oauthId;
    }
    public String nickname(){
        return nickname;
    }
    public String profile(){
        return profile;
    }
    public String email(){
        return email;
    }


}
