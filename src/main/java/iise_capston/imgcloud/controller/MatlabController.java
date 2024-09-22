package iise_capston.imgcloud.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MatlabController {
    public static void main(String[] args) {
        try {
            // Specify the path to the MATLAB executable and the input image path
            String executablePath = "C:/iise_capston/imgcloud2/computeBrisqueExe2.exe"; // Update with your executable path
            String imagePath = "C:/iise_capston/imgcloud2/goof.jpg"; // Update with your image path

            // Run the executable with the image path as an argument
            ProcessBuilder processBuilder = new ProcessBuilder(executablePath, imagePath);

            // Start the process
            Process process = processBuilder.start();

            // Capture the output from the executable
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Capture any errors from the executable
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }

            // Wait for the process to finish
            int exitCode = process.waitFor();
            System.out.println("Exited with code: " + exitCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


