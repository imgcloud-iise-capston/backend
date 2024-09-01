package iise_capston.imgcloud.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import java.util.Base64;

@RequiredArgsConstructor
@Service
public class GetDataResponseTransformer {

    private final S3Client s3Client;

    public String getDataResponse(String bucket, String key) {
        return getObjectBase64(bucket, key);
    }

    public String getObjectBase64(String bucketName, String keyName) {
        try {
            // S3 객체 요청 생성
            GetObjectRequest objectRequest = GetObjectRequest.builder()
                    .key(keyName)
                    .bucket(bucketName)
                    .build();

            // S3 객체의 바이트 데이터를 가져옴
            ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObject(objectRequest, software.amazon.awssdk.core.sync.ResponseTransformer.toBytes());
            byte[] data = objectBytes.asByteArray();

            // 바이트 데이터를 Base64 문자열로 변환
            String base64Data = Base64.getEncoder().encodeToString(data);
            System.out.println("Successfully obtained bytes from an S3 object and encoded to Base64");

            return base64Data;

        } catch (Exception e) {
            System.err.println("Error occurred while downloading file from S3: " + e.getMessage());
            return null; // 에러 발생 시 null 반환 또는 예외 던지기
        }
    }
}