package iise_capston.imgcloud.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class downloadService {
    private String iniBucketName = "imgcloud-iise";
    String bucketNamePerson ="person/";
    String bucketNameThing = "thing/";


    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    public String presignedDownloadUrl(String type, String key) {
        String s3url = "";
        System.out.println(accessKey);



        try (S3Presigner presigner = S3Presigner.builder()
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .region(Region.AP_NORTHEAST_2).build()) {
            String bucketName = type.equals("person") ? bucketNamePerson : bucketNameThing;

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(iniBucketName)
                    //.key("person/Cost.png2024-10-11T12:47:05.736475500Z")
                    .key(bucketName+key.trim())
                    .build();

            System.out.println(getObjectRequest);

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10)) // URL 만료시간 설정
                    .getObjectRequest(getObjectRequest)
                    .build();

            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
            s3url = presignedRequest.url().toString();
        }

        return s3url;
    }



}
