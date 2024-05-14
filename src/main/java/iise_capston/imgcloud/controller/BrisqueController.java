package iise_capston.imgcloud.controller;

import iise_capston.imgcloud.service.BrisqueService;
import iise_capston.imgcloud.service.SvmTrainService;
import lombok.RequiredArgsConstructor;
import org.opencv.core.Core;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class BrisqueController {

    private final SvmTrainService svmTrainService;
    private final BrisqueService brisqueService;
    Logger logger = LoggerFactory.getLogger(BrisqueController.class);

    @PostMapping("/make/train/svm")
    ResponseEntity<String> makeTrainSvm(
            @RequestPart("trainData") MultipartFile trainData
            ){

        svmTrainService.saveSvmModel(trainData);

        return ResponseEntity.ok("savedNewModel");
    }

    @PostMapping("/calculate/brisque")
    ResponseEntity<String> calBrisque(
            @RequestPart("image") MultipartFile image
    ){
        double score = 0;
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        try{
            logger.info("run");
            score = brisqueService.test_measure_BRISQUE(image);
            logger.info("run end");
        }catch (IOException e){}

        logger.info("score : "+score);
        return ResponseEntity.ok("ok");
    }


}
