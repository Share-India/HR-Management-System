package com.hr.system.service;

import com.hr.system.entity.Document;
import com.hr.system.repository.DocumentRepository;
import com.hr.system.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final EmployeeRepository employeeRepository;
    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public Document storeFile(MultipartFile file, Long employeeId, String type) {
        // Normalize file name
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileName = UUID.randomUUID().toString() + "_" + originalFileName;

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            Document document = new Document();
            document.setFileName(originalFileName);
            document.setFilePath(targetLocation.toString());
            document.setType(type);
            document.setEmployee(employeeRepository.findById(employeeId).orElseThrow());

            return documentRepository.save(document);
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public List<Document> getDocumentsByEmployee(Long employeeId) {
        return documentRepository.findByEmployeeId(employeeId);
    }

    public org.springframework.core.io.Resource getDocumentFile(Long documentId) {
        try {
            Document doc = documentRepository.findById(documentId)
                    .orElseThrow(() -> new RuntimeException("Document not found"));
            Path filePath = Paths.get(doc.getFilePath());
            org.springframework.core.io.Resource resource = new org.springframework.core.io.UrlResource(
                    filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found " + doc.getFileName());
            }
        } catch (Exception ex) {
            throw new RuntimeException("File not found", ex);
        }
    }
}
