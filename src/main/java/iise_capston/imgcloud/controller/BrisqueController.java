package iise_capston.imgcloud.controller;

import iise_capston.imgcloud.service.SvmTrainService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class BrisqueController {

    private final SvmTrainService svmTrainService;
    Logger logger = LoggerFactory.getLogger(BrisqueController.class);

    @PostMapping("/make/train/svm")
    ResponseEntity<String> makeTrainSvm(
            @RequestPart("trainData") MultipartFile trainData
            ){

        svmTrainService.saveSvmModel(trainData);


        return ResponseEntity.ok("savedNewModel");
    }



}
