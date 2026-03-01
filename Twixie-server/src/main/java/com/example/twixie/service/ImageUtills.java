package com.example.twixie.service;

import org.mapstruct.Named;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class ImageUtills {
    private static String UPLOAD_DIRECTORY = System.getProperty("user.dir")+ "\\images\\";

    public static void uploadImage(MultipartFile file) throws IOException {
        String path=UPLOAD_DIRECTORY+file.getOriginalFilename();
        Path fileName = Paths.get(path);
        Files.write(fileName, file.getBytes());
    }

    public static String getImage(String fileName) throws IOException{
        try {
            Path filePath = Paths.get(UPLOAD_DIRECTORY + fileName);
            byte[] byteFile = Files.readAllBytes(filePath);
            return Base64.getEncoder().encodeToString(byteFile);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

}
