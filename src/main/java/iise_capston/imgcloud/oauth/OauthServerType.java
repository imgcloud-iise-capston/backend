package iise_capston.imgcloud.oauth;

import static java.util.Locale.ENGLISH;

//어떤 인증 서버를 사용할 것인지 명시 나중에 확장했을 때 편하게 찾아오기 위함
public enum OauthServerType {

    KAKAO,
    NAVER,
    ;
    public static OauthServerType fromName(String type){
        return OauthServerType.valueOf(type.toUpperCase(ENGLISH));
    }
}
