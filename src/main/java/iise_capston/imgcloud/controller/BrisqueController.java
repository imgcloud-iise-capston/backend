package iise_capston.imgcloud.controller;

import iise_capston.imgcloud.domain.dto.*;
import iise_capston.imgcloud.domain.repository.PeopleImageMemberRepository;
import iise_capston.imgcloud.member.OauthMember;
import iise_capston.imgcloud.member.PeopleImageMember;
import iise_capston.imgcloud.member.ThingImageMember;
import iise_capston.imgcloud.service.BrisqueService;
import iise_capston.imgcloud.service.MetadataService;
import iise_capston.imgcloud.service.OauthService;
import iise_capston.imgcloud.service.SmallFileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.objectweb.asm.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
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
    private final MetadataService metadataService;
    private final ObjectMapper objectMapper;

    private final PeopleImageMemberRepository peopleImageMemberRepository;
    Logger logger = LoggerFactory.getLogger(BrisqueController.class);


    @PostMapping(value = "/calculate/transformed/person")
    public ResponseEntity<Integer> calTransformedBrisque(
            @RequestPart("image") MultipartFile image,
            @RequestPart("imageId") Long imageId,
            @RequestPart("fileType") String fileType
    )throws IOException {

        long startTime = System.currentTimeMillis();


        PeopleImageMember peopleImageMember = peopleImageMemberRepository.findByPeopleId(imageId).get();

        int finalScores = 0;

        double x = 0;
        double y = 0;
        double width = 0;
        double height = 0;


        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // Map<String, Object> cropData = objectMapper.readValue(cropDataJson, Map.class);

            x = peopleImageMember.getX();
            y = peopleImageMember.getY();
            width = peopleImageMember.getWidth();
            height = peopleImageMember.getHeight();


            System.out.println(x + " " + y + " " + width + " " + height);
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(image.getBytes()));
            width = Math.min(width, originalImage.getWidth() - x);
            height = Math.min(height, originalImage.getHeight() - y);
            BufferedImage croppedImage = originalImage.getSubimage((int) x, (int) y, (int) width, (int) height);

            int len = fileType.length();
            fileType = fileType.substring(1,len-1);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(croppedImage, fileType, baos);

            byte[] croppedBytes = baos.toByteArray();


            MultipartFile croppedMultipartFile = new MockMultipartFile("croppedImage", "croppedImage." + fileType, "image/" + fileType, croppedBytes);
            List<MultipartFile> croppedImages = new ArrayList<>();
            croppedImages.add(croppedMultipartFile);

            List<CompletableFuture<Scalar>> completablescores = brisqueService.getBrisqueAll(croppedImages);
            for (CompletableFuture<Scalar> cnow : completablescores) {
                Scalar now = cnow.get();
                double score = Math.round(now.get(0));
                finalScores = (100 - (int) score);
            }
        } catch (IOException e) {
            logger.error("Error reading image or parsing cropData", e);
            return ResponseEntity.status(500).body(null);
        } catch (Exception e) {
            logger.info("Error calculating BRISQUE scores " + e);
            return ResponseEntity.status(500).body(null);
        }

        long endTime = System.currentTimeMillis(); // 끝나는 시간 기록
        System.out.println("Person Time : " + (endTime - startTime) + " ms"); // 실행 시간 출력

        return ResponseEntity.ok(finalScores);
    }

    @PostMapping("/calculate/transformed/thing")
    public ResponseEntity<Integer> calTransformedThingBri(
            @RequestPart("image") MultipartFile image
    ){

        long startTime = System.currentTimeMillis(); // 끝나는 시간 기록

        List<MultipartFile> listImage = new ArrayList<>();
        listImage.add(image);

        CompletableFuture<Scalar> completablescores;
        int finalScore=0;

        try {
            completablescores = brisqueService.getBrisqueAll(listImage).get(0);
            Scalar now = completablescores.get();
            double score = Math.round(now.get(0));
            finalScore = 100 - (int)score;

        } catch (Exception e) {
            logger.error("Error calculating BRISQUE scores", e);
            return ResponseEntity.status(500).build();
        }

        long endTime = System.currentTimeMillis(); // 끝나는 시간 기록
        System.out.println("Thing  time: " + (endTime - startTime) + " ms"); // 실행 시간 출력
        return ResponseEntity.ok(finalScore);
    }


    //Thing 점수 cal + img 저장
    //title 중복이어도 ok -> key, brisque에 대한 중복 처리 완료
    @PostMapping("/calculate/brisque")
    public ResponseEntity<List<Integer>> calBrisque(
            @RequestPart("image") List<MultipartFile> image,
            @RequestPart("smallFiles") List<MultipartFile> smallFiles,
            @RequestPart("imageTitle") String titles,
            @RequestHeader("userId") Long userId,
            @RequestPart("FStop") String FStop,
            @RequestPart("ISO") String ISO,
            @RequestPart("ExposureTime") String ExposureTime,
            @RequestPart("GPSLatitude") String GPSLatitude,
            @RequestPart("GPSLongitude") String GPSLongitude,
            @RequestPart("RealResolution") String RealResolution,
            @RequestPart("Resolution") String Resolution,
            @RequestPart("WhiteBalance") String WhiteBalance,
            @RequestPart("size") String size,
            @RequestPart("metaScore") String metaScore
    ) throws IOException {
        List<CompletableFuture<Scalar>> completablescores;
        List<Integer> finalScores = new ArrayList<>();

        TitleDto titleDto = objectMapper.readValue(titles,TitleDto.class);
        //ThingMetadataDto thingMetadataDto2 = objectMapper.readValue(metadata, ThingMetadataDto.class);

        logger.info("userId"+userId);
        List<Double> FStop2 = objectMapper.readValue(FStop,new com.fasterxml.jackson.core.type.TypeReference<List<Double>>(){});
        List<Integer> ISO2 = objectMapper.readValue(ISO,new com.fasterxml.jackson.core.type.TypeReference<List<Integer>>(){});
        List<Integer> ExposureTime2 = objectMapper.readValue(ExposureTime, new com.fasterxml.jackson.core.type.TypeReference<List<Integer>>() {
        });
        List<String> GPSLatitude2 = objectMapper.readValue(GPSLatitude,new com.fasterxml.jackson.core.type.TypeReference<List<String>>(){});
        List<String> GPSLongitude2 = objectMapper.readValue(GPSLongitude,new com.fasterxml.jackson.core.type.TypeReference<List<String>>(){});
        List<String> RealResolution2 = objectMapper.readValue(RealResolution,new com.fasterxml.jackson.core.type.TypeReference<List<String>>(){});
        List<String> Resolution2 = objectMapper.readValue(Resolution,new com.fasterxml.jackson.core.type.TypeReference<List<String>>(){});
        List<String> WhiteBalance2 = objectMapper.readValue(WhiteBalance,new com.fasterxml.jackson.core.type.TypeReference<List<String>>(){});
        List<String> size2 = objectMapper.readValue(size, new com.fasterxml.jackson.core.type.TypeReference<List<String>>() {});

        List<String> title = titleDto.getTitles();

        List<Integer> metaScore2 = objectMapper.readValue(metaScore,new com.fasterxml.jackson.core.type.TypeReference<List<Integer>>() {});

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

        ThingMetadataDto thingMetadataDto = new ThingMetadataDto();
        thingMetadataDto.setISO(ISO2);
        thingMetadataDto.setExposureTime(ExposureTime2);
        thingMetadataDto.setResolution(Resolution2);
        thingMetadataDto.setFStop(FStop2);
        thingMetadataDto.setGPSLongitude(GPSLongitude2);
        thingMetadataDto.setGPSLatitude(GPSLatitude2);
        thingMetadataDto.setRealResolution(RealResolution2);
        thingMetadataDto.setWhiteBalance(WhiteBalance2);
        thingMetadataDto.setSize(size2);
        thingMetadataDto.setMetaScore(metaScore2);


        List<CompletableFuture<Long>> metaId = new ArrayList<>();
        metaId = smallFileService.uploadThingImages(thingImageUploadDto, thingMetadataDto);
        //metadataService.saveMetaData(metadataDto,thingImageUploadDto.getThingImageMember());



        return ResponseEntity.ok(finalScores);
    }

    //한개씩만 업로드 가능 -> 한개씩으로
    @PostMapping("/calculate/personBrisque")
    @Transactional
    public ResponseEntity<List<Integer>> calPersonBrisque(
            @RequestPart("image") MultipartFile image,
            @RequestPart("cropData") String cropDataJson,
            @RequestPart("fileType") String fileType, // 파일 확장자 받기
            @RequestPart("smallFiles") List<MultipartFile> smallFiles,
            @RequestPart("imageTitle") String titles,
            @RequestHeader("userId") Long userId,
            @RequestPart("FStop") Double FStop,
            @RequestPart("ISO") Integer ISO,
            @RequestPart("ExposureTime") Integer ExposureTime,
            @RequestPart("GPSLatitude") String GPSLatitude,
            @RequestPart("GPSLongitude") String GPSLongitude,
            @RequestPart("RealResolution") String RealResolution,
            @RequestPart("Resolution") String Resolution,
            @RequestPart("WhiteBalance") String WhiteBalance,
            @RequestPart("size") String size,
            @RequestPart("metaScore") Integer metaScore
    ) throws IOException {
        List<Integer> finalScores = new ArrayList<>();
        OauthMember uploadedUser = oauthService.findUploadUser(userId);
        if (uploadedUser == null) {
            return ResponseEntity.badRequest().build();
        }

        TitleDto titleDto = objectMapper.readValue(titles, TitleDto.class);
        List<String> title = titleDto.getTitles();

        double x = 0;
        double y = 0;
        double width = 0;
        double height = 0;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> cropData = objectMapper.readValue(cropDataJson, Map.class);

            x = convertToDouble(cropData.get("x"));
            y = convertToDouble(cropData.get("y"));
            width = convertToDouble(cropData.get("width"));
            height = convertToDouble(cropData.get("height"));
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(image.getBytes()));
            width = Math.min(width, originalImage.getWidth() - x);
            height = Math.min(height, originalImage.getHeight() - y);
            BufferedImage croppedImage = originalImage.getSubimage((int) x, (int) y, (int) width, (int) height);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(croppedImage, fileType, baos);
            byte[] croppedBytes = baos.toByteArray();

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
            logger.info("Error calculating BRISQUE scores " + e);
            return ResponseEntity.status(500).body(null);
        }

        PeopleImageMember peopleImageMemberSave = new PeopleImageMember();
        PeopleImageUploadDto peopleImageUploadDto = new PeopleImageUploadDto();
        peopleImageUploadDto.setX(x);
        peopleImageUploadDto.setY(y);
        peopleImageUploadDto.setWidth(width);
        peopleImageUploadDto.setHeight(height);
        peopleImageUploadDto.setFileType(fileType);
        peopleImageUploadDto.setBigImageFiles(image);
        peopleImageUploadDto.setSmallImageFiles(smallFiles);
        peopleImageUploadDto.setOauthMember(uploadedUser);
        peopleImageUploadDto.setImageTitle(title);
        peopleImageUploadDto.setBrisqueScore(finalScores);
        peopleImageMemberSave.setUserPeopleId(uploadedUser);
        peopleImageUploadDto.setPeopleImageMember(peopleImageMemberSave);

        PeopleMetadataDto peopleMetadataDto = new PeopleMetadataDto();
        peopleMetadataDto.setISO(ISO);
        peopleMetadataDto.setExposureTime(ExposureTime);
        peopleMetadataDto.setResolution(Resolution);
        peopleMetadataDto.setFStop(FStop);
        peopleMetadataDto.setGPSLongitude(GPSLongitude);
        peopleMetadataDto.setGPSLatitude(GPSLatitude);
        peopleMetadataDto.setRealResolution(RealResolution);
        peopleMetadataDto.setWhiteBalance(WhiteBalance);
        peopleMetadataDto.setSize(size);
        peopleMetadataDto.setMetaScore(metaScore);

        smallFileService.uploadPeopleImages(peopleImageUploadDto, peopleMetadataDto);

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