package com.arpita.jobportal.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir:uploads/resumes}")
    private String uploadDir;

    public String storeFile(MultipartFile file, Long userId) {
        try {
            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null ?
                    originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
            String filename = "resume_" + userId + "_" + UUID.randomUUID() + extension;

            // Copy file to upload directory
            Path targetLocation = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/resumes/" + filename;

        } catch (IOException ex) {
            throw new RuntimeException("Could not store file. Please try again!", ex);
        }
    }

    public void deleteFile(String fileUrl) {
        try {
            if (fileUrl != null && !fileUrl.isEmpty()) {
                Path filePath = Paths.get(uploadDir).resolve(
                        fileUrl.substring(fileUrl.lastIndexOf("/") + 1)
                );
                Files.deleteIfExists(filePath);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Could not delete file", ex);
        }
    }
}