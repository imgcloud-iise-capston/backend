package iise_capston.imgcloud.service;

import lombok.RequiredArgsConstructor;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.UMat;
import org.bytedeco.opencv.opencv_quality.QualityBRISQUE;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.*;

import org.opencv.core.MatOfByte;

import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class BrisqueService {
    private final String modelPath = "src/main/resources/brisque/brisque_model_live.yml";
    private final String rangePath = "src/main/resources/brisque/brisque_range_live.yml";

    private Logger logger = LoggerFactory.getLogger(BrisqueService.class);
    @Async
    @Transactional
    public CompletableFuture<Scalar> getBrisqueOne(MultipartFile picture) throws IOException {

        String basePath = "src/main/resources/brisque";
        Path copyOfLocation = Paths.get(basePath+File.pathSeparator+StringUtils.cleanPath(picture.getOriginalFilename()));
        Files.copy(picture.getInputStream(), copyOfLocation, StandardCopyOption.REPLACE_EXISTING);

        Mat pic = opencv_imgcodecs.imread(copyOfLocation.toString());

        org.bytedeco.opencv.opencv_core.Mat pp = new org.bytedeco.opencv.opencv_core.Mat();

        QualityBRISQUE brisque = QualityBRISQUE.create(modelPath,rangePath);
        Scalar brisqueScore = brisque.compute(pic);

        return CompletableFuture.completedFuture(brisqueScore);
    }

    public List<CompletableFuture<Scalar>> getBrisqueAll(List<MultipartFile> pictures) throws IOException{
        List<CompletableFuture<Scalar>> brisqueScores= new ArrayList<>();

        for(int i=0;i<pictures.size();i++){
            CompletableFuture<Scalar> score = getBrisqueOne(pictures.get(i));
            brisqueScores.add(score);
        }

        return brisqueScores;
    }
}
