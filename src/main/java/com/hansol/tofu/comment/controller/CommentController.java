package com.hansol.tofu.comment.controller;

import com.hansol.tofu.clubmember.annotation.IsMember;
import com.hansol.tofu.comment.CommentService;
import com.hansol.tofu.comment.domain.dto.CommentCreationRequestDTO;
import com.hansol.tofu.comment.domain.dto.CommentEditRequestDTO;
import com.hansol.tofu.global.BaseHttpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "comment", description = "동호회 댓글 API")
@RestController
@RequestMapping("/api/clubs")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "동호회 댓글 작성 API", responses = {
            @ApiResponse(responseCode = "200", description = "동호회 댓글 작성 성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "400", description = "입력정보 누락(내용)", content = @Content(schema = @Schema(implementation = Long.class))),
    })
    @IsMember
    @PostMapping("/{clubId}/boards/{boardId}/comments")
    public BaseHttpResponse<Long> addComment(
            @PathVariable Long clubId,
            @PathVariable Long boardId,
            @Valid @RequestBody CommentCreationRequestDTO commentCreationRequestDTO
    ) {
        return BaseHttpResponse.success(commentService.addComment(boardId, commentCreationRequestDTO));
    }

    @Operation(summary = "동호회 댓글 수정 API", responses = {
            @ApiResponse(responseCode = "200", description = "동호회 게시글 수정 성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "400", description = "입력정보 누락(내용, 내용)", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "403", description = "본인이 아닌 다른 회원 게시글 수정 시도", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 댓글", content = @Content(schema = @Schema(implementation = BaseHttpResponse.class))),
    })
    @IsMember
    @PatchMapping("/{clubId}/boards/{boardId}/comments/{commentId}")
    public BaseHttpResponse<?> editComment(
            @PathVariable Long clubId,
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @RequestBody @Valid CommentEditRequestDTO commentEditRequestDTO) {
        commentService.editComment(boardId, commentId, commentEditRequestDTO);
		return BaseHttpResponse.successWithNoContent();
    }


    @Operation(summary = "동호회 댓글 삭제 API", responses = {
            @ApiResponse(responseCode = "200", description = "동호회 댓글 삭제 성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "400", description = "입력정보 누락(내용, 내용)", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "403", description = "본인이 아닌 다른 회원 댓글 삭제 시도", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 댓글", content = @Content(schema = @Schema(implementation = BaseHttpResponse.class))),
    })
    @IsMember
    @DeleteMapping("/{clubId}/boards/{boardId}/comments/{commentId}")
    public BaseHttpResponse<?> deleteComment(
            @PathVariable Long clubId,
            @PathVariable Long boardId,
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(boardId, commentId);
		return BaseHttpResponse.successWithNoContent();
    }


}
