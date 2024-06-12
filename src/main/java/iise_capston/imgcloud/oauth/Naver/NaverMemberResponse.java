package iise_capston.imgcloud.oauth.Naver;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import iise_capston.imgcloud.oauth.OauthId;
import iise_capston.imgcloud.member.OauthMember;

import static iise_capston.imgcloud.oauth.OauthServerType.NAVER;


@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record NaverMemberResponse(
        String resultcode,
        String message,
        Response response
) {
    public OauthMember toDomain(){
        return OauthMember.builder()
                .oauthId(new OauthId(NAVER))
                .nickname(response.nickname)
                .email(response.email)
                .profile(response.profileImageUrl)
                .build();
    }
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Response(
            String id,
            String nickname,
            String email,
            String profileImageUrl
    ){
    }
}
