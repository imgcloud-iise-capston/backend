package iise_capston.imgcloud.controller;

import iise_capston.imgcloud.domain.dto.PeopleImageUploadDto;
import iise_capston.imgcloud.domain.dto.ThingImageUploadDto;
import iise_capston.imgcloud.domain.dto.TitleDto;
import iise_capston.imgcloud.member.OauthMember;
import iise_capston.imgcloud.member.PeopleImageMember;
import iise_capston.imgcloud.member.ThingImageMember;
import iise_capston.imgcloud.service.BrisqueService;
import iise_capston.imgcloud.service.OauthService;
import iise_capston.imgcloud.service.SmallFileService;
import lombok.RequiredArgsConstructor;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import com.fasterxml.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
@RestController
public class BrisqueController {
    private final BrisqueService brisqueService;
    private final OauthService oauthService;
    private final SmallFileService smallFileService;
    private final ObjectMapper objectMapper;
    Logger logger = LoggerFactory.getLogger(BrisqueController.class);

    //Thing 점수 cal + img 저장
    //title 중복이어도 ok -> key, brisque에 대한 중복 처리 완료
    @PostMapping("/calculate/brisque")
    public ResponseEntity<List<Integer>> calBrisque(
            @RequestPart("image") List<MultipartFile> image,
            @RequestPart("smallFiles") List<MultipartFile> smallFiles,
            @RequestPart("imageTitle") String titles,
            @RequestHeader("userId") Long userId
    ) throws IOException {
        List<CompletableFuture<Scalar>> completablescores;
        List<Integer> finalScores = new ArrayList<>();

        TitleDto titleDto = objectMapper.readValue(titles,TitleDto.class);

        List<String> title = titleDto.getTitles();

        //어떤 사용자가 올린 건지 계산
        ThingImageMember InithingImageMember = new ThingImageMember();
        OauthMember uploadedUser = oauthService.findUploadUser(userId);
        if(uploadedUser==null){
            logger.info("user not found");
            return ResponseEntity.badRequest().build();
        }
        InithingImageMember.setUserThingId(uploadedUser);
        List<CompletableFuture<String>> url =  new ArrayList<>();

        try {
            completablescores = brisqueService.getBrisqueAll(image);

            for (CompletableFuture<Scalar> cnow : completablescores) {
                Scalar now = cnow.get();
                double score = Math.round(now.get(0));
                finalScores.add((100 - (int) score));

            }

        } catch (Exception e) {
            logger.error("Error calculating BRISQUE scores", e);
            return ResponseEntity.status(500).build();
        }

        //사진 저장 로직, title, key, url, user 저장 -> brisque score 저장 X -> brisque 테이블에 연결되어 있기 떄문(중복 저장 X)
        ThingImageUploadDto thingImageUploadDto = new ThingImageUploadDto();
        thingImageUploadDto.setBigImageFiles(image);
        thingImageUploadDto.setSmallImageFiles(smallFiles);
        thingImageUploadDto.setOauthMember(uploadedUser);
        thingImageUploadDto.setImageTitle(title);
        thingImageUploadDto.setBrisqueScore(finalScores);
        thingImageUploadDto.setThingImageMember(InithingImageMember);

        url = smallFileService.uploadThingImages(thingImageUploadDto);


        return ResponseEntity.ok(finalScores);
    }

    //한개씩만 업로드 가능 -> 한개씩으로
    @PostMapping("/calculate/personBrisque")
    public ResponseEntity<List<Integer>> calPersonBrisque(
            @RequestPart("image") MultipartFile image,
            @RequestPart("cropData") String cropDataJson,
            @RequestPart("fileType") String fileType, // 파일 확장자 받기
            @RequestPart("smallFiles") List<MultipartFile> smallFiles,
            @RequestPart("imageTitle") String titles,
            @RequestHeader("userId") Long userId
    ) throws IOException{
        List<Integer> finalScores = new ArrayList<>();

        //어떤 사용자가 올린 건지 계산
        OauthMember uploadedUser = oauthService.findUploadUser(userId);
        if(uploadedUser==null){
            logger.info("user not found");
            return ResponseEntity.badRequest().build();
        }
        double x = 0;
        double y = 0;
        double width = 0;
        double height = 0;

        TitleDto titleDto = objectMapper.readValue(titles,TitleDto.class);
        List<String> title = titleDto.getTitles();


        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> cropData = objectMapper.readValue(cropDataJson, Map.class);

            x = convertToDouble(cropData.get("x"));
            y = convertToDouble(cropData.get("y"));
            width = convertToDouble(cropData.get("width"));
            height = convertToDouble(cropData.get("height"));
            System.out.println("x: "+x+ "y: "+y+"width: "+width);

            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(image.getBytes()));
            BufferedImage croppedImage = originalImage.getSubimage((int) x, (int) y, (int) width, (int) height);

            // 크롭된 이미지를 바이트 배열로 변환
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(croppedImage, fileType, baos); // 파일 확장자 사용
            byte[] croppedBytes = baos.toByteArray();

            // 바이트 배열을 MultipartFile로 변환
            MultipartFile croppedMultipartFile = new MockMultipartFile("croppedImage", "croppedImage." + fileType, "image/" + fileType, croppedBytes);

            List<MultipartFile> croppedImages = new ArrayList<>();
            croppedImages.add(croppedMultipartFile);

            List<CompletableFuture<Scalar>> completablescores = brisqueService.getBrisqueAll(croppedImages);

            for (CompletableFuture<Scalar> cnow : completablescores) {
                Scalar now = cnow.get();
                double score = Math.round(now.get(0));
                finalScores.add((100 - (int) score));
            }

        } catch (IOException e) {
            logger.error("Error reading image or parsing cropData", e);
            return ResponseEntity.status(500).body(null);
        } catch (Exception e) {
            logger.info("Error calculating BRISQUE scores "+e);
            return ResponseEntity.status(500).body(null);
        }

        List<CompletableFuture<String>> url =  new ArrayList<>();

        //사진 저장 로직, title, key, url, user 저장 -> brisque score 저장 안하는 게 좋을 듯
        PeopleImageUploadDto peopleImageUploadDto = new PeopleImageUploadDto();
        peopleImageUploadDto.setBigImageFiles(image);
        peopleImageUploadDto.setSmallImageFiles(smallFiles);
        peopleImageUploadDto.setOauthMember(uploadedUser);
        peopleImageUploadDto.setImageTitle(title);
        peopleImageUploadDto.setBrisqueScore(finalScores);
        peopleImageUploadDto.setX(x);
        peopleImageUploadDto.setY(y);
        peopleImageUploadDto.setWidth(width);
        peopleImageUploadDto.setHeight(height);
        peopleImageUploadDto.setFileType(fileType);
        url = smallFileService.uploadPeopleImages(peopleImageUploadDto);

        return ResponseEntity.ok(finalScores);
    }

    private double convertToDouble(Object value) {
        if (value instanceof Integer) {
            return ((Integer) value).doubleValue();
        } else if (value instanceof BigDecimal) {
            return ((BigDecimal) value).doubleValue();
        } else if (value instanceof Double) {
            return (Double) value;
        } else {
            throw new IllegalArgumentException("Unsupported number type");
        }
    }
}