package iise_capston.imgcloud.oauth.kakao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

//네이밍 규칙 token_type -> tokenType 이렇게 바꿔줌
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoToken (
    String tokenType,
    String accessToken,
    String idToken,
    Integer expiresIn,
    String refreshToken,
    Integer refreshTokenExpiresIn,
    String scope
){
}
