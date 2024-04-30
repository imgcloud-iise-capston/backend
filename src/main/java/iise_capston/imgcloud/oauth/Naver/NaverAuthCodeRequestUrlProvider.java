package iise_capston.imgcloud.oauth.Naver;

import iise_capston.imgcloud.oauth.AuthCodeRequestUrlProvider;
import iise_capston.imgcloud.oauth.OauthServerType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class NaverAuthCodeRequestUrlProvider implements AuthCodeRequestUrlProvider {
    private final NaverOauthConfig naverOauthConfig;

    @Override
    public OauthServerType supportServer(){
        return OauthServerType.NAVER;
    }
    @Override
    public String provide(){
        return UriComponentsBuilder
                .fromUriString("http://nid.naver.com/oauth2.0/authorize")
                .queryParam("response_type","code")
                .queryParam("client_id",naverOauthConfig.clientId())
                .queryParam("redirect_uri",naverOauthConfig.redirectUri())
                .queryParam("state",naverOauthConfig.state())
                .toUriString();
    }
}
