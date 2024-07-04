package iise_capston.imgcloud;

import nu.pattern.OpenCV;
import org.opencv.core.Core;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableTransactionManagement
public class ImgcloudApplication {

	private static final Logger logger = LoggerFactory.getLogger(ImgcloudApplication.class);
	public static void main(String[] args) {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		SpringApplication.run(ImgcloudApplication.class, args);
	}
}
