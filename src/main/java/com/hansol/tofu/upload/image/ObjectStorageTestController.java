package com.hansol.tofu.upload.image;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "storage", description = "스토리지 API")
@RestController
@RequestMapping("/api/auth/storage")
@RequiredArgsConstructor
public class ObjectStorageTestController {

    private final StorageService storageService;

    @GetMapping("/list")
    public void callList() {
        storageService.getBuckets();
    }
}
