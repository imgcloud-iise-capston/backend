package iise_capston.imgcloud.controller;

import iise_capston.imgcloud.service.BrisqueService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.analysis.function.Abs;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@RestController
public class BrisqueController {
    private final BrisqueService brisqueService;
    Logger logger = LoggerFactory.getLogger(BrisqueController.class);

//    @PostMapping("/make/train/svm")
//    ResponseEntity<String> makeTrainSvm(
//            @RequestPart("trainData") MultipartFile trainData
//            ){
//
//        svmTrainService.saveSvmModel(trainData);
//
//        return ResponseEntity.ok("savedNewModel");
//    }

    @PostMapping("/calculate/brisque")
    ResponseEntity<String> calBrisque(
            @RequestPart("image") List<MultipartFile> image
    ) throws IOException{

        List<CompletableFuture<Scalar>> completablescores = brisqueService.getBrisqueAll(image);

        List<Integer> finalscores = new ArrayList<>();
        try{
            for(int i=0; i<completablescores.size();i++){
                CompletableFuture<Scalar> cnow = completablescores.get(i);
                Scalar now = cnow.get();
                double score = Math.round(now.get(0));
                finalscores.add((100-(int)score));
            }

        }catch (Exception e){}


        logger.info("score : "+finalscores.get(0));
        return ResponseEntity.ok("ok");
    }


}
