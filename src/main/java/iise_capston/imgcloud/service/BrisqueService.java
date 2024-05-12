package iise_capston.imgcloud.service;

import iise_capston.imgcloud.brisque.libsvm.*;
import iise_capston.imgcloud.brisque.svm_train;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class BrisqueService {
    public static void main(String[]args){

        // 데이터 파일 경로
        String dataFilePath = "C:/iise_capston/imgcloud2/backend/src/main/java/iise_capston/imgcloud/brisque/allmodel2.txt";

        // svm_problem 객체 생성 및 데이터 파일로부터 데이터 로드
        svm_problem prob = loadProblem(dataFilePath);

        // svm_parameter 객체 생성 및 설정
        svm_parameter param = new svm_parameter();
        param.svm_type = svm_parameter.EPSILON_SVR;
        param.kernel_type = svm_parameter.RBF;
        param.gamma = 0.05; // 예시 파라미터
        param.C = 1; // C 값 설정

        // 모델 학습
        svm_model model = svm.svm_train(prob, param);

        // 모델 저장 또는 사용
        try {
            svm.svm_save_model("C:/iise_capston/imgcloud2/backend/src/main/java/iise_capston/imgcloud/service/testSVM.txt", model);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    // 파일로부터 svm_problem 객체 로드
    private static svm_problem loadProblem(String dataFilePath) {
        svm_problem prob = new svm_problem();
        try {
            BufferedReader br = new BufferedReader(new FileReader(dataFilePath));
            String line;
            ArrayList<Double> labels = new ArrayList<>();
            ArrayList<svm_node[]> nodes = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");
                labels.add(Double.parseDouble(st.nextToken()));
                int m = st.countTokens() / 2;
                svm_node[] x = new svm_node[m];
                for (int j = 0; j < m; j++) {
                    x[j] = new svm_node();
                    x[j].index = Integer.parseInt(st.nextToken());
                    x[j].value = Double.parseDouble(st.nextToken());
                }
                nodes.add(x);
            }
            br.close();

            prob.l = labels.size();
            prob.y = new double[prob.l];
            prob.x = new svm_node[prob.l][];
            for (int i = 0; i < prob.l; i++) {
                prob.y[i] = labels.get(i);
                prob.x[i] = nodes.get(i);
            }
        } catch (IOException e) {
            System.err.println("Error reading data file: " + e.getMessage());
            System.exit(1);
        }
        return prob;
    }
}
