package iise_capston.imgcloud.service;

import iise_capston.imgcloud.brisque.libsvm.svm;
import iise_capston.imgcloud.brisque.libsvm.svm_model;
import iise_capston.imgcloud.brisque.libsvm.svm_node;
import lombok.RequiredArgsConstructor;

import org.apache.commons.math3.special.Gamma;

<<<<<<< HEAD
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
=======
import nu.pattern.OpenCV;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgcodecs.*;
>>>>>>> 86cc70295e9e9daa4b60b613ec2eea6cf61a4cf7

import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

<<<<<<< HEAD
=======
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedReader;
import java.io.File;
>>>>>>> 86cc70295e9e9daa4b60b613ec2eea6cf61a4cf7
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
<<<<<<< HEAD
public class BrisqueServiceBackup {
    private Logger logger = LoggerFactory.getLogger(BrisqueServiceBackup.class);
=======
public class BrisqueService {
    private Logger logger = LoggerFactory.getLogger(BrisqueService.class);
>>>>>>> 86cc70295e9e9daa4b60b613ec2eea6cf61a4cf7


    public double test_measure_BRISQUE(MultipartFile multiImg) throws IOException {
        double qualityScore = 0;
        //File file = new File("C:/iise_capston/imgcloud2/backend/test11/jpg");

        //multiImg.transferTo(file);
        //여기 파일 받는 부분 만들어야 함

//        BufferedImage br = ImageIO.read(multiImg.getInputStream());

//        BufferedImage imageCopy = new BufferedImage(br.getWidth(), br.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
//        logger.info("a22222");
//        imageCopy.getGraphics().drawImage(br, 0, 0, null);

//        byte[] data = ((DataBufferByte) imageCopy.getRaster().getDataBuffer()).getData();
//        Mat img = new Mat(br.getHeight(),br.getWidth(),CvType.CV_8UC3);
//        img.put(0,0,data);


        Mat img = Imgcodecs.imread("C:/iise_capston/imgcloud2/backend/test11.jpg", Imgcodecs.IMREAD_COLOR);

        try {
            //convert to gray scale
            Mat grayImg = new Mat();
            Imgproc.cvtColor(img, grayImg, Imgproc.COLOR_BGR2GRAY);

            //compute feature vectors of the image
            List<Double> features = compute_features(grayImg);
            logger.info("a" + features.get(0));

            //rescale the brisqueFeatures vector from -1 to 1
            List<Double> x = new ArrayList<>();

            //preloaded lists from C++ Module to rescale brisquefeatures vector to [-1, 1]
            double[] min_ = {0.336999, 0.019667, 0.230000, -0.125959, 0.000167, 0.000616, 0.231000, -0.125873, 0.000165, 0.000600, 0.241000, -0.128814, 0.000179, 0.000386, 0.243000, -0.133080, 0.000182, 0.000421, 0.436998, 0.016929, 0.247000, -0.200231, 0.000104, 0.000834, 0.257000, -0.200017, 0.000112, 0.000876, 0.257000, -0.155072, 0.000112, 0.000356, 0.258000, -0.154374, 0.000117, 0.000351};
            double[] max_ = {9.999411, 0.807472, 1.644021, 0.202917, 0.712384, 0.468672, 1.644021, 0.169548, 0.713132, 0.467896, 1.553016, 0.101368, 0.687324, 0.533087, 1.554016, 0.101000, 0.689177, 0.533133, 3.639918, 0.800955, 1.096995, 0.175286, 0.755547, 0.399270, 1.095995, 0.155928, 0.751488, 0.402398, 1.041992, 0.093209, 0.623516, 0.532925, 1.042992, 0.093714, 0.621958, 0.534484};

            logger.info("a3");
            //append the rescaled vector to x
            for (int i = 0; i < 36; i++) {
                double min = min_[i];
                double max = max_[i];
                x.add(-1 + (2.0 / (max - min) * (features.get(i) - min)));
            }

            svm_node[] x_node = new svm_node[37];

            for (int i = 0; i < 36; i++) {
                x_node[i] = new svm_node(); // 각 요소를 생성
                x_node[i].index = 1; //kernel_type == PRECOMPUTED 이면 1 아니면 0 -> 일단 1로 설정
                x_node[i].value = x.get(i);
            }
            x_node[36] = new svm_node();
            x_node[36].index = -1;

            logger.info("0 value : " + x_node[0].value);

            //load model + calculate Score
            try {
                svm_model model = svm.svm_load_model("C:/iise_capston/imgcloud2/backend/src/main/java/iise_capston/imgcloud/service/testSVM.model");

                String svm_type = "epsilon_svr";
                double[] is_prob_model = model.probA;
                int nr_class = model.nr_class;

                int nr_classifier = 1;  //type=epsilon_svr로 고정
                double dec_values = (double) nr_classifier;

                qualityScore = svm.svm_predict_probability(model, x_node, is_prob_model);
                logger.info("qualityScore : " + qualityScore);

            } catch (IOException e) {
                logger.info(e + "can't calculate score");
            }

        } catch (CvException e) {
            logger.info(e + "wrong img");
        }

        return qualityScore;
    }

    public List<Double> compute_features(Mat grayImg) {
        List<Double> feat = new ArrayList<>();

        int scaleNum = 2;

        Mat im_original = new Mat();
        grayImg.copyTo(im_original);

        //Imgcodecs.imwrite("C:/iise_capston/imgcloud2/backend/check.jpg", im_original);



        //scale img twice
        for (int i = 0; i < scaleNum; i++) {
            Mat im = new Mat();
            im_original.copyTo(im);
            //normalize the image
            Core.normalize(im, im, 0, 255.0, Core.NORM_MINMAX, CvType.CV_64F);
            //calculate MSCN coefficients
            Mat mu = new Mat();
            Size size = new Size(7, 7);
            Imgproc.GaussianBlur(im, mu, size, 1.166);

            Mat mu_sq = new Mat(im.size(), CvType.CV_64F);
            //mu_sq = mu.mul(mu);
            Core.multiply(mu, mu, mu_sq);

            Mat sigma = new Mat(im.size(), CvType.CV_64F);
            Mat im2 = new Mat(im.size(), CvType.CV_64F);
            Core.multiply(im, im, im2);
            Imgproc.GaussianBlur(im2, sigma, size, 1.166);

            Core.subtract(sigma, mu_sq, sigma);

            sigma.convertTo(sigma, CvType.CV_64F, 1.0 / 255.0);

            Core.sqrt(sigma, sigma);

            logger.info("b1");
            //structdis is the MSCN img
            Mat structdis = new Mat(im.size(), CvType.CV_64F);
            Core.subtract(im, mu, structdis);
            logger.info("b0");
            Core.add(sigma, Scalar.all(1.0 / 255), sigma);
            logger.info("b2");

            sigma.convertTo(sigma, CvType.CV_64F);
            Core.divide(structdis, sigma, structdis);

            //logger.info(structdis.dump());
            logger.info("channels : " + structdis.row(0));
            logger.info("channels : " + structdis.col(0));


            logger.info("b3");
            Imgcodecs.imwrite("C:/iise_capston/imgcloud2/backend/check.jpg", structdis);

            //calculate best fitted parameters from MSCN image
            double[] best_fit_params = AGGDfit(structdis);

            //unwrap the best fit params
            double lsigma_best = best_fit_params[0];
            double rsigma_best = best_fit_params[1];
            double gamma_best = best_fit_params[2];

            logger.info(lsigma_best + "ddd");

            //append the best fit parameters for MSCN image
            feat.add(gamma_best);
            feat.add((lsigma_best * lsigma_best + rsigma_best * rsigma_best) / 2);

            //shifting indices for creating pair-wise products
            int[][] shifts = {{0, 1}, {1, 0}, {1, 1}, {-1, 1}};

            for (int j = 1; j < shifts.length + 1; j++) {
                Mat OrigArr = structdis;
                int[] reqshift = shifts[j - 1];

                //create transformation matrix for warpAffine function
//                double[][] data = {{1, 0, reqshift[1]}, {0, 1, reqshift[0]}};
//                Array2DRowRealMatrix M = new Array2DRowRealMatrix(data);
                Mat M = new Mat(2, 3, CvType.CV_32F);
                M.put(0, 0, 1.0, 0.0, reqshift[1]);
                M.put(1, 0, 0.0, 1.0, reqshift[0]);
                Mat ShiftArr = new Mat();
                Size size_or = OrigArr.size();
                Imgproc.warpAffine(OrigArr, ShiftArr, M, size_or);

                Mat Shifted_new_structdis = ShiftArr;
                Core.multiply(Shifted_new_structdis, structdis, Shifted_new_structdis);

                best_fit_params = AGGDfit(Shifted_new_structdis);
                lsigma_best = best_fit_params[0];
                rsigma_best = best_fit_params[1];
                gamma_best = best_fit_params[2];

                double constant = Math.pow(Gamma.gamma(1 / gamma_best), 0.5) / Math.pow(Gamma.gamma(3 / gamma_best), 0.5);
                double meanparam = (rsigma_best - lsigma_best) * (Gamma.gamma(2 / gamma_best) / Gamma.gamma(1 / gamma_best)) * constant;
                feat.add(gamma_best);  //gamma best
                feat.add(meanparam);  //mean shape
                feat.add(Math.pow(lsigma_best, 2)); //left variance square
                feat.add(Math.pow(rsigma_best, 2));  //right variance square
            }

            //resize the image on next iteration
            Size newSize = new Size(im_original.cols() * 0.5, im_original.rows() * 0.5);
            Mat resizedImage = new Mat();
            Imgproc.resize(im_original, resizedImage, newSize, 0, 0, Imgproc.INTER_CUBIC);

        }
        return feat;
    }

    public double func(double gam, double prevgamma, double prevdiff, double sampling, double rhatnorm) {

        while (gam < 10) {
            double r_gam = Gamma.gamma(2 / gam) * Gamma.gamma(2 / gam) / (Gamma.gamma(1 / gam) * Gamma.gamma(3 / gam));
            double diff = Math.abs(r_gam - rhatnorm);
            if (diff > prevdiff)
                break;
            prevdiff = diff;
            prevgamma = gam;
            gam += sampling;
        }
        double gamma_best = prevgamma;

        return gamma_best;
    }

    //AGGD fit model, takes input as the MSCN Image / Pair-wise Product
    public double[] AGGDfit(Mat structdis) {
        //variables to count positive pixels / negative pixels and their squared sum
        int poscount = 0; // number of positive pixels
        int negcount = 0; // number of negative pixels
        double possqsum = 0.0;
        double negsqsum = 0.0;
        double abssum = 0.0;

//        Mat img = Imgcodecs.imread("C:/iise_capston/imgcloud2/backend/test2.jpg",Imgcodecs.IMREAD_COLOR);
//        img.copyTo(structdis);

        int channels = structdis.channels();

        //calculate squared sum of positive pixels and negative pixels
        // absolute squared sum

        logger.info("channel : " + channels);
        structdis.convertTo(structdis, CvType.CV_64FC3);
        int size = (int) (structdis.total() * channels);
        double[] temp = new double[size];
        structdis.get(0, 0, temp);

        for (int i = 0; i < size; i++) {
            double value = temp[i];
            //logger.info("value : "+value);
            if (value > 0) {
                poscount++;
                possqsum += value * value;
                abssum += value;
            } else if (value < 0) {
                negcount++;
                negsqsum += value * value;
                abssum += Math.abs(value);
            }
        }


        //calculate left sigma variance and right sigma variance
        double lsigma_best = Math.sqrt((negsqsum / negcount));
        double rsigma_best = Math.sqrt((possqsum / poscount));

        double gammahat = lsigma_best / rsigma_best;

        // total number of pixels - totalcount
        int totalcount = structdis.rows() * structdis.cols() * channels;

        double rhat = Math.pow(abssum / totalcount, 2) / ((negsqsum + possqsum) / totalcount);
        double rhatnorm = rhat * (Math.pow(gammahat, 3) + 1) * (gammahat + 1) / (Math.pow(Math.pow(gammahat, 2) + 1, 2));

        double prevgamma = 0;
        double prevdiff = 1e10; // =10000000000.0
        double sampling = 0.001;
        double gam = 0.2;

        //vectorized function call for best fitting parameters
        // calculate best fit params
        double gamma_best = func(gam, prevgamma, prevdiff, sampling, rhatnorm);

        double[] ret = new double[3];
        ret[0] = lsigma_best;
        ret[1] = rsigma_best;
        ret[2] = gamma_best;


        return ret;


    }

}
