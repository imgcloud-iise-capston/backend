package iise_capston.imgcloud.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import iise_capston.imgcloud.domain.dto.PeopleImageUploadDto;
import iise_capston.imgcloud.domain.dto.PeopleMetadataDto;
import iise_capston.imgcloud.domain.dto.ThingImageUploadDto;
import iise_capston.imgcloud.domain.dto.ThingMetadataDto;
import iise_capston.imgcloud.domain.repository.PeopleImageMemberRepository;
import iise_capston.imgcloud.domain.repository.ThingImageMemberRepository;
import iise_capston.imgcloud.member.PeopleImageMember;
import iise_capston.imgcloud.member.ThingImageMember;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class SmallFileService {
    private String iniBucketName = "imgcloud-iise";
    String bucketNamePerson =iniBucketName+"/person";
    String bucketNameThing = iniBucketName+"/thing";

    private final BrisqueService brisqueService;
    private final AmazonS3Client amazonS3Client;
    private final PeopleImageMemberRepository peopleImageMemberRepository;
    private final ThingImageMemberRepository thingImageMemberRepository;

    private final MetadataService metadataService;
    private Logger logger = LoggerFactory.getLogger(SmallFileService.class);

    public ThingImageMember findThingImage(String title){
        Optional<ThingImageMember> thingImageMember = thingImageMemberRepository.findByimageTitle(title);
        if (thingImageMember.isPresent()) {
            ThingImageMember thingImage = thingImageMember.get();
            return thingImage;
        } else {
            return null;
        }
    }
    public PeopleImageMember findPeopleImage(String title){
        Optional<PeopleImageMember> peopleImageMember = peopleImageMemberRepository.findByimageTitle(title);
        if (peopleImageMember.isPresent()) {
            PeopleImageMember peopleImage = peopleImageMember.get();
            return peopleImage;
        } else {
            return null;
        }
    }

    //img 여러개 처리
    public List<CompletableFuture<Long>> uploadPeopleImages(PeopleImageUploadDto peopleImageUploadDto, PeopleMetadataDto peopleMetadataDto){
        List<CompletableFuture<Long>> resultList = new ArrayList<>();

        int i=0;

        //1개 처리 임시
        PeopleImageMember peopleImage = peopleImageUploadDto.getPeopleImageMember();
        logger.info("height in peopleImages : " + peopleImageUploadDto.getPeopleImageMember().getHeight());
        MultipartFile big = peopleImageUploadDto.getBigImageFiles();
        MultipartFile small = peopleImageUploadDto.getSmallImageFiles().get(0);
        String title = peopleImageUploadDto.getImageTitle().get(0);
        Integer brisque = peopleImageUploadDto.getBrisqueScore().get(0);
        double x = peopleImageUploadDto.getX();
        double y = peopleImageUploadDto.getY();
        double width = peopleImageUploadDto.getWidth();
        double height = peopleImageUploadDto.getHeight();

        CompletableFuture<Long> value = uploadPeopleImage(brisque, title,big, small, peopleImage, x, y, width, height, peopleMetadataDto);
        resultList.add(value);

//        for(MultipartFile big: peopleImageUploadDto.getBigImageFiles()){
//            PeopleImageMember peopleImage = peopleImageUploadDto.getPeopleImageMember();
//            MultipartFile small = peopleImageUploadDto.getSmallImageFiles().get(i);
//            String title = peopleImageUploadDto.getImageTitle().get(i);
//
//            CompletableFuture<String> value = uploadPeopleImage(title,big, small, peopleImage, i);
//            resultList.add(value);
//            i++;
//        }

        return resultList;
    }
    public List<CompletableFuture<Long>> uploadThingImages(ThingImageUploadDto thingImageUploadDto, ThingMetadataDto thingMetadataDto){
        List<CompletableFuture<Long>> resultList = new ArrayList<>();

        int i=0;
        for(MultipartFile big: thingImageUploadDto.getBigImageFiles()){
            ThingImageMember thingImage = thingImageUploadDto.getThingImageMember();
            MultipartFile small = thingImageUploadDto.getSmallImageFiles().get(i);
            Integer brisque = thingImageUploadDto.getBrisqueScore().get(i);
            String title = thingImageUploadDto.getImageTitle().get(i);

            Double fstop = thingMetadataDto.getFStop().get(i);
            Integer iso = thingMetadataDto.getISO().get(i);
            Integer exposureTime = thingMetadataDto.getExposureTime().get(i);
            String resolution = thingMetadataDto.getResolution().get(i);
            String realResolution = thingMetadataDto.getRealResolution().get(i);
            String gpsLatitude = thingMetadataDto.getGPSLatitude().get(i);
            String gpsLongitude = thingMetadataDto.getGPSLongitude().get(i);
            String whiteBalance = thingMetadataDto.getWhiteBalance().get(i);
            String size = thingMetadataDto.getSize().get(i);
            Integer metaScore = thingMetadataDto.getMetaScore().get(i);

            CompletableFuture<Long> value = uploadThingImage(brisque, title, big, small, thingImage, i, fstop,iso,exposureTime,realResolution,resolution,gpsLatitude,
                    gpsLongitude,whiteBalance, size, metaScore);
            resultList.add(value);
            i++;
        }

        return resultList;
    }

    //img 1개
    @Transactional
    @Async
    public CompletableFuture<Long> uploadPeopleImage(Integer score, String title, MultipartFile big, MultipartFile small, PeopleImageMember image, double x, double y,
                                                       double width, double height, PeopleMetadataDto peopleMetadataDto){
        PeopleImageMember peopleImageMember = new PeopleImageMember();
        //peopleImageMember = peopleImageMemberRepository.findById(image.getPeopleId()).get();
        peopleImageMember.setUserPeopleId(image.getUserPeopleId());
        peopleImageMember.setX(x);
        peopleImageMember.setY(y);
        peopleImageMember.setHeight(height);
        peopleImageMember.setWidth(width);

        String titleAdd = title;
        //key = 사용자 입력 title + 저장 시간 -> 이걸로 불러오기 때문에 중복을 피하기 위해서
        Instant now = Instant.now();
        String key = titleAdd.concat(now.toString());
        peopleImageMember.setImageKey(key);
        peopleImageMember.setImageTitle(titleAdd);

        String smallKey = key.concat("small");
        peopleImageMember.setSmallImageKey(smallKey);

        try{
            InputStream bigImage = big.getInputStream();
            InputStream smallImage = small.getInputStream();

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(big.getContentType());
            objectMetadata.setContentLength(big.getSize());

            ObjectMetadata objectMetadataSmall = new ObjectMetadata();
            objectMetadataSmall.setContentType(small.getContentType());
            objectMetadataSmall.setContentLength(small.getSize());

            amazonS3Client.putObject(bucketNamePerson,key,bigImage,objectMetadata);

            String accessUrl = amazonS3Client.getUrl(bucketNamePerson,key).toString();
            peopleImageMember.setImageUrl(accessUrl);

            amazonS3Client.putObject(bucketNamePerson,smallKey,smallImage,objectMetadataSmall);
            String smallUrl = amazonS3Client.getUrl(bucketNamePerson,smallKey).toString();
            peopleImageMember.setSmallImageUrl(smallUrl);

        } catch (IOException e){

        }

        Double fstop = peopleMetadataDto.getFStop();
        Integer iso = peopleMetadataDto.getISO();
        Integer exposureTime = peopleMetadataDto.getExposureTime();
        String resolution = peopleMetadataDto.getResolution();
        String realResolution = peopleMetadataDto.getRealResolution();
        String gpsLatitude = peopleMetadataDto.getGPSLatitude();
        String gpsLongitude = peopleMetadataDto.getGPSLongitude();
        String whiteBalance = peopleMetadataDto.getWhiteBalance();
        String size = peopleMetadataDto.getSize();
        Integer metaScore = peopleMetadataDto.getMetaScore();

        peopleImageMemberRepository.save(peopleImageMember);
        brisqueService.savePeopleBrisque(peopleImageMember,score);
        long metaId = metadataService.savePeopleMetaData(fstop,iso, exposureTime, realResolution, resolution, gpsLatitude,
                gpsLongitude, whiteBalance, peopleImageMember, size, metaScore);


        return CompletableFuture.completedFuture(metaId);
    }


    @Transactional
    @Async
    public CompletableFuture<Long> uploadThingImage(Integer score, String title, MultipartFile big, MultipartFile small, ThingImageMember image, int num,
                                                      Double fstop,Integer iso, Integer exposureTime, String realResolution, String resolution, String gpsLatitude,
                                                      String gpsLongitude, String whiteBalance, String size, Integer metaScore) {
        ThingImageMember thingImageMember = new ThingImageMember();
        thingImageMember.setUserThingId(image.getUserThingId());

        String titleAdd = title;
        //key = 사용자 입력 title + 저장 시간 -> 이걸로 불러오기 때문에 중복을 피하기 위해서
        Instant now = Instant.now();
        String key = titleAdd.concat(now.toString());
        thingImageMember.setImageKey(key);
        thingImageMember.setImageTitle(titleAdd);

        String smallKey = key.concat("small");
        thingImageMember.setSmallImageKey(smallKey);

        try {
            InputStream bigVideo = big.getInputStream();
            InputStream smallVideo = small.getInputStream();

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(big.getContentType());
            objectMetadata.setContentLength(big.getSize());

            ObjectMetadata objectMetadataSmall = new ObjectMetadata();
            objectMetadataSmall.setContentType(small.getContentType());
            objectMetadataSmall.setContentLength(small.getSize());

            amazonS3Client.putObject(bucketNameThing, key, bigVideo, objectMetadata);

            String accessUrl = amazonS3Client.getUrl(bucketNameThing, key).toString();
            thingImageMember.setImageUrl(accessUrl);

            amazonS3Client.putObject(bucketNameThing, smallKey, smallVideo, objectMetadataSmall);
            String smallUrl = amazonS3Client.getUrl(bucketNameThing, smallKey).toString();
            thingImageMember.setSmallImageUrl(smallUrl);

        } catch (IOException e) {

        }

        thingImageMemberRepository.save(thingImageMember);
        brisqueService.saveThingBrisque(thingImageMember, score);
        long metaId = metadataService.saveThingMetaData(fstop,iso, exposureTime, realResolution, resolution, gpsLatitude,
                gpsLongitude, whiteBalance, thingImageMember, size, metaScore);

        return CompletableFuture.completedFuture(metaId);
    }


}
