package iise_capston.imgcloud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ImgcloudApplication {
	private static final Logger logger = LoggerFactory.getLogger(ImgcloudApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(ImgcloudApplication.class, args);
	}
}
