package com.hansol.tofu.clubphoto.controller;

import com.hansol.tofu.clubphoto.ClubPhotoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "photo", description = "동호회 사진 API")
@RestController
@RequestMapping("/api/club-photo")
@RequiredArgsConstructor
public class ClubPhotoController {
    private final ClubPhotoService clubPhotoService;


}
