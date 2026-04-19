package com.example.BookSocialNetwork.file;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.BiFunction;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
@Slf4j
public class FileStorageService {
    @Value("${application.file.upload.photos-output-path}")
    private String fileUploadPath ;

    public String saveFile(@NotNull MultipartFile sourceFile, @NotNull Integer userId) {
        final String fileUploadSubPath= "users"+ File.separator+ userId;
        return uploadFile.apply(sourceFile,fileUploadSubPath);
    }



    private final Function<String,String> getFileExtension= (fileName)->{
        if (fileName == null || fileName.isEmpty()) {
            return " ";
        }
        int lastDotIndex = fileName.lastIndexOf(".");

        if (lastDotIndex == -1) {
            return "";
        }
        return fileName.substring(lastDotIndex+1).toLowerCase();
    };



    private final BiFunction<@NotNull MultipartFile, String,String> uploadFile =(sourceFile, fileUploadSubPath)->{
        String finalUploadPath= fileUploadPath+File.separator+fileUploadSubPath;
        File targetFolder = new File(finalUploadPath);

        if (!targetFolder.exists()) {
            boolean folderCreated = targetFolder.mkdir();

            if (!folderCreated) {
                log.warn("Failed to create the target folder");
            }
        }

        final String fileExtension = getFileExtension.apply(sourceFile.getOriginalFilename());
        String targetFilePath= fileUploadPath+File.separator+System.currentTimeMillis()+"."+fileExtension;
        Path targetPath = Paths.get(targetFilePath);

        try {
            Files.write(targetPath,sourceFile.getBytes());
            log.info("File saved to target file path");
            return targetFilePath;
        }
        catch (IOException ex){
            log.error("File was not saved",ex);
        }

        return null;
    };

}
