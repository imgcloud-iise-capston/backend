package iise_capston.imgcloud.oauth;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access=PROTECTED)
public class OauthId {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "server_type")
    private OauthServerType oauthServerType;

    public OauthServerType oauthServer(){
        return oauthServerType;
    }
}
