package iise_capston.imgcloud.oauth;

//인터페이스
//AuthCode를 발급할 URL 제공 -> 카카오, 네이버 어디?
public interface AuthCodeRequestUrlProvider {
    //지원하는 서버인지 판단
    OauthServerType supportServer();

    //서버 타입에 맞는 URL 반환
    String provide();
}
