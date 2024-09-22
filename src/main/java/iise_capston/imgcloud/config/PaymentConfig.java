package iise_capston.imgcloud.config;

import com.siot.IamportRestClient.IamportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConfig {
    String apiKey = "2648716441255517";
    String secretKey = "U5I9hyU9pufbSyNBkLOtTugovOBY747ReIKCfQS42JqwPVhCoRBHC1hevuN5oXPVigDXBQBTNnSdy3EU";

    @Bean
    public IamportClient iamportClient() {
        return new IamportClient(apiKey, secretKey);
    }
}
