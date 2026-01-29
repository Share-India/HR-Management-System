package com.hr.system.controller;

import com.hr.system.entity.Document;
import com.hr.system.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService service;

    @PostMapping("/upload")
    public ResponseEntity<Document> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("employeeId") Long employeeId,
            @RequestParam("type") String type) {
        return ResponseEntity.ok(service.storeFile(file, employeeId, type));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Document>> getDocumentsByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(service.getDocumentsByEmployee(employeeId));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<org.springframework.core.io.Resource> downloadFile(@PathVariable Long id) {
        org.springframework.core.io.Resource file = service.getDocumentFile(id);
        return ResponseEntity.ok()
                .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}
