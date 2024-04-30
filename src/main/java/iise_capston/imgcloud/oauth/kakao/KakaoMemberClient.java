package iise_capston.imgcloud.oauth.kakao;


import iise_capston.imgcloud.member.OauthMember;
import iise_capston.imgcloud.oauth.OauthMemberClient;
import iise_capston.imgcloud.oauth.OauthServerType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
@RequiredArgsConstructor
public class KakaoMemberClient implements OauthMemberClient {

    private final KakaoApiClient kakaoApiClient;
    private final KakaoOauthConfig kakaoOauthConfig;

    @Override
    public OauthServerType supportServer(){
        return OauthServerType.KAKAO;
    }
    @Override
    public OauthMember fetch(String authCode){
        //AuthCode를 가지고 AccessToken 가져오기
        KakaoToken tokenInfo = kakaoApiClient.fetchToken(tokenRequestParams(authCode));
        //AccessToken 가지고 회원 정보 받아오기(KakaoMemberResponse에 가서 내가 정한 형태로 받아오는 거임)
        KakaoMemberResponse kakaoMemberResponse = kakaoApiClient.fetchMember("Bearer "+tokenInfo.accessToken());
        //OauthMember 객체로 반환해주기
        return kakaoMemberResponse.toDomain();
    }
    private MultiValueMap<String, String> tokenRequestParams(String authCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoOauthConfig.clientId());
        params.add("redirect_uri", kakaoOauthConfig.redirectUri());
        params.add("code", authCode);
        params.add("client_secret", kakaoOauthConfig.clientSecret());
        return params;
    }
}
