package iise_capston.imgcloud.controller;

import iise_capston.imgcloud.domain.repository.PeopleImageMemberRepository;
import iise_capston.imgcloud.domain.repository.ThingImageMemberRepository;
import iise_capston.imgcloud.member.PeopleImageMember;
import iise_capston.imgcloud.member.ThingImageMember;
import iise_capston.imgcloud.service.GetDataResponseTransformer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

@RequiredArgsConstructor
@RestController
public class UrlToImageController {

    private final GetDataResponseTransformer getDataResponseTransformer;
    private final PeopleImageMemberRepository peopleImageMemberRepository;
    private final ThingImageMemberRepository thingImageMemberRepository;

    Logger logger = LoggerFactory.getLogger(UrlToImageController.class);

    public static String encodeFileToBase64(File file) throws IOException {
        // 파일의 바이트 배열을 읽음
        byte[] fileContent = Files.readAllBytes(file.toPath());
        // 바이트 배열을 Base64로 인코딩
        return Base64.getEncoder().encodeToString(fileContent);
    }

    @PostMapping("/base64")
    ResponseEntity<String> sendImage(
            @RequestParam("id") Long id,
            @RequestParam("detail") String detail) throws IOException{


        if(detail.equals("person")){
            PeopleImageMember peopleImageMember = peopleImageMemberRepository.findById(id).get();
            String path = peopleImageMember.getImageUrl();
            String key = peopleImageMember.getImageKey();

            String finalKey = detail+"/"+key;
            String peopleFile = getDataResponseTransformer.getDataResponse("imgcloud-iise",finalKey);


            return ResponseEntity.ok(peopleFile);
        }

        if(detail.equals("thing")){
            ThingImageMember thingImageMember = thingImageMemberRepository.findById(id).get();
            String path = thingImageMember.getImageUrl();
            String key = thingImageMember.getImageKey();

            String finalKey = detail+"/"+key;
            String thingFile = getDataResponseTransformer.getDataResponse("imgcloud-iise",finalKey);

            return ResponseEntity.ok(thingFile);
        }

        return ResponseEntity.ok("no");
    }
}
