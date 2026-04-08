package com.app.library.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class CoverStorageService {

    private static final List<String> ALLOWED_TYPES = List.of("image/jpeg", "image/png", "image/webp");

    private final Path coverUploadPath;

    public CoverStorageService(@Value("${app.upload.dir:uploads}") String uploadDir) {
        this.coverUploadPath = Paths.get(uploadDir, "covers").toAbsolutePath().normalize();
    }

    public String storeCover(MultipartFile file, String existingPublicPath) throws IOException {
        if (file == null || file.isEmpty()) {
            return existingPublicPath;
        }

        validateImage(file);
        Files.createDirectories(coverUploadPath);

        String extension = getExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID() + extension;
        Path destination = coverUploadPath.resolve(fileName).normalize();

        if (!destination.startsWith(coverUploadPath)) {
            throw new IllegalArgumentException("Ficheiro inválido.");
        }

        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        deleteByPublicPath(existingPublicPath);
        return "/uploads/covers/" + fileName;
    }

    public void deleteByPublicPath(String publicPath) throws IOException {
        if (!StringUtils.hasText(publicPath) || !publicPath.startsWith("/uploads/covers/")) {
            return;
        }

        String fileName = publicPath.substring("/uploads/covers/".length());
        if (!StringUtils.hasText(fileName)) {
            return;
        }

        Path filePath = coverUploadPath.resolve(fileName).normalize();
        if (filePath.startsWith(coverUploadPath)) {
            Files.deleteIfExists(filePath);
        }
    }

    private void validateImage(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("Formato inválido. Utilize JPG, PNG ou WEBP.");
        }

        if (file.getSize() > 2 * 1024 * 1024) {
            throw new IllegalArgumentException("Imagem muito grande. Limite: 2MB.");
        }
    }

    private String getExtension(String originalFileName) {
        String cleanName = StringUtils.cleanPath(originalFileName == null ? "" : originalFileName);
        int dotIndex = cleanName.lastIndexOf('.');
        if (dotIndex < 0) {
            return ".jpg";
        }
        return cleanName.substring(dotIndex);
    }
}

