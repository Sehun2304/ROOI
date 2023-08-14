package com.rooi.rooi.service;

import com.rooi.rooi.dto.CommentRequestDto;
import com.rooi.rooi.dto.CommentResponseDto;
import com.rooi.rooi.entity.Card;
import com.rooi.rooi.entity.Comment;
import com.rooi.rooi.entity.User;
import com.rooi.rooi.repository.CardRepository;
import com.rooi.rooi.repository.CommentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.RejectedExecutionException;

@Service
@AllArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;

    public CommentResponseDto createComment(CommentRequestDto requestDto, User user) {
        Card card = cardRepository.findById(requestDto.getCard_id()).orElseThrow();
        Comment comment = new Comment(requestDto, card, user);  //댓글내용,작성자,작성글 담음

        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }
    public void updateComment(Long commentId, CommentRequestDto requestDto, User user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new RejectedExecutionException("작성자만 수정 가능합니다");
        }
        log.info(requestDto.getContent());
        comment.setContents(requestDto.getContent());
        commentRepository.save(comment);
    }

    public void deleteComment(Long id, User user) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다"));
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new RejectedExecutionException("작성자만 삭제 가능합니다");
        }
        commentRepository.delete(comment);
    }
}