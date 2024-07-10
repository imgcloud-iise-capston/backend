package iise_capston.imgcloud.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import iise_capston.imgcloud.domain.dto.ThingImageUploadDto;
import iise_capston.imgcloud.domain.dto.ThingMetadataDto;
import iise_capston.imgcloud.domain.dto.TitleDto;
import iise_capston.imgcloud.domain.repository.PeopleImageMemberRepository;
import iise_capston.imgcloud.member.OauthMember;
import iise_capston.imgcloud.member.ThingImageMember;
import iise_capston.imgcloud.service.BrisqueService;
import iise_capston.imgcloud.service.MetadataService;
import iise_capston.imgcloud.service.OauthService;
import iise_capston.imgcloud.service.SmallFileService;
import lombok.RequiredArgsConstructor;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static net.minidev.asm.DefaultConverter.convertToDouble;

@RequiredArgsConstructor
@RestController
public class ContinuousImageController {
    private final BrisqueService brisqueService;
    private final OauthService oauthService;
    private final SmallFileService smallFileService;
    private final MetadataService metadataService;
    private final ObjectMapper objectMapper;

    private final PeopleImageMemberRepository peopleImageMemberRepository;
    Logger logger = LoggerFactory.getLogger(BrisqueController.class);

    @PostMapping("/calculate/continuousImage")
    public ResponseEntity<List<Integer>> calBrisque(
            @RequestPart("image") List<MultipartFile> image,
            @RequestHeader("userId") Long userId
    ) throws IOException {
        List<CompletableFuture<Scalar>> completablescores;
        List<Integer> finalScores = new ArrayList<>();

        //어떤 사용자가 올린 건지 계산
        OauthMember uploadedUser = oauthService.findUploadUser(userId);
        if (uploadedUser == null) {
            logger.info("user not found");
            return ResponseEntity.badRequest().build();
        }

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


        return ResponseEntity.ok(finalScores);
    }

    @PostMapping("/calculate/continuousImage/people")
    public ResponseEntity<List<Integer>> calBrisquePeople(
            @RequestPart("images") List<MultipartFile> images,
            @RequestPart("cropData") String cropDataJson,
            @RequestPart("fileType") String fileType,
            @RequestHeader("userId") Long userId
    ) throws IOException {
        List<Integer> finalScores = new ArrayList<>();
        OauthMember uploadedUser = oauthService.findUploadUser(userId);
        if (uploadedUser == null) {
            return ResponseEntity.badRequest().build();
        }



        for(int i=0;i<images.size();i++){
            double x = 0;
            double y = 0;
            double width = 0;
            double height = 0;
            MultipartFile image = images.get(i);
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
        }


        return ResponseEntity.ok(finalScores);
    }

}
