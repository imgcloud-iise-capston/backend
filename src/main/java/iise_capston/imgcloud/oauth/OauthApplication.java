package iise_capston.imgcloud.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan  //@configurationproperties (KakaoOauthConfig) 사용하기 위해 필요
public class OauthApplication {
    public static void main(String[]args){
        SpringApplication.run(OauthApplication.class, args);
    }
}
