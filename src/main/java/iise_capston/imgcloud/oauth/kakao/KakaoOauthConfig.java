package iise_capston.imgcloud.oauth.kakao;

import org.springframework.boot.context.properties.ConfigurationProperties;

//application.properties에서 정보 읽어옴
@ConfigurationProperties(prefix = "oauth.kakao")
public record KakaoOauthConfig(
        String redirectUri,
        String clientId,
        String clientSecret,
        String[] scope
) {
}
