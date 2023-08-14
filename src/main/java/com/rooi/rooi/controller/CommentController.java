package com.rooi.rooi.controller;

import com.rooi.rooi.dto.ApiResponseDto;
import com.rooi.rooi.dto.CommentRequestDto;
import com.rooi.rooi.dto.CommentResponseDto;
import com.rooi.rooi.security.UserDetailsImpl;
import com.rooi.rooi.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.RejectedExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/card/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("")   //댓글 작성
    public CommentResponseDto createComment(@RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.createComment(commentRequestDto, userDetails.getUser());
    }
    @PutMapping("/{comment_Id}")    //댓글 수정
    public ResponseEntity<ApiResponseDto> updateComment(@PathVariable Long comment_Id, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        try {
            commentService.updateComment(comment_Id, commentRequestDto, userDetails.getUser());
        } catch (RejectedExecutionException e) {
            ApiResponseDto apiResponseDto = new ApiResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(apiResponseDto, HttpStatus.BAD_REQUEST);
        }
        ApiResponseDto apiResponseDto = new ApiResponseDto("댓글을 수정했습니다.", HttpStatus.OK.value());
        return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{comment_Id}") //    삭제
    public ResponseEntity<ApiResponseDto> deleteComment(@PathVariable Long comment_Id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        try {
            commentService.deleteComment(comment_Id, userDetails.getUser());
        } catch (RejectedExecutionException | IllegalArgumentException e) {
            ApiResponseDto apiResponseDto = new ApiResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(apiResponseDto, HttpStatus.BAD_REQUEST);
        }
        ApiResponseDto apiResponseDto = new ApiResponseDto("댓글을 삭제했습니다.", HttpStatus.OK.value());
        return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
    }
}