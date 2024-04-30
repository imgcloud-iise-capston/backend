package iise_capston.imgcloud.oauth;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public class AuthCodeRequestUrlProviderComposite {

    //서버를 지원하는 각 AuthCodeRequestUrlProvider 구현체를 서버 이름에 매핑 -> <KAKAO, ~~>
    private final Map<OauthServerType, AuthCodeRequestUrlProvider> mapping;

    public AuthCodeRequestUrlProviderComposite(Set<AuthCodeRequestUrlProvider> providers){
        mapping = providers.stream()   //providers 집합을 스트림으로 변환
                .collect(toMap(     //스트림의 요소를 모아서 Map으로 변환
                        AuthCodeRequestUrlProvider::supportServer,   //AuthCodeRequestUrlProvider 구현체의 supportServer 메소드를 이용해 서버 지원하는지 판단
                        identity()  //주어진 입력을 그대로 출력해주는 함수 (5 넣으면 5, "h"넣으면 "h")
                ));
    }
    public String provide(OauthServerType oauthServerType) {
        return getProvider(oauthServerType).provide(); //반환해온 서버타입의 provide()메소드 불러와서 해당 메소드의 로그인 url 제공
        //KAKAO.provide();
    }
    private AuthCodeRequestUrlProvider getProvider(OauthServerType oauthServerType) {
        return Optional.ofNullable(mapping.get(oauthServerType)) //누락될 수 없는 정보 mapping에 해당 서버의 provider있는지 확인
                .orElseThrow(() -> new RuntimeException("지원하지 않는 소셜 로그인 타입입니다.")); //servertype에 없는 정보일 경우
    }
}
