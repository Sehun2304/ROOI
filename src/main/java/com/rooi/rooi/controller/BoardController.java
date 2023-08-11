package com.rooi.rooi.controller;

import com.rooi.rooi.dto.*;
import com.rooi.rooi.security.UserDetailsImpl;
import com.rooi.rooi.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {

	private final BoardService boardService;

	// 내가 작성한 전체 보드 정보 가져오기
	@GetMapping // URL은 마음대로 수정해서 사용하세요 @@
	public List<BoardResponseDto> getAllMyBoards(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		log.info("Controller - getAllMyBoards");
		return boardService.getAllMyBoards(userDetails.getUser());
	}

	// 내가 작성한 특정 보드 정보 가져오기
	@GetMapping("/{id}")
	public ResponseEntity<?> getBoardById(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(boardService.getBoardById(id));
	}

	// 보드 생성
	@PostMapping
	public ResponseEntity<?> createBoard(@RequestBody BoardRequestDto requestDto,
	                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
		log.info("Controller - createBoard");
		BoardResponseDto result = boardService.createBoard(requestDto, userDetails.getUser());
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	// 보드 수정
	@PutMapping("/{id}")
	public ResponseEntity<?> updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto,
	                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
		try {
			BoardResponseDto result = boardService.updateBoard(id, requestDto, userDetails.getUser());
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
		}
	}

	// 보드 삭제
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteBoard(@PathVariable Long id,
	                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
		try {
			ApiResponseDto result = boardService.deleteBoard(id, userDetails.getUser());
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
		}
	}

	// 보드에 유저 초대
	@PostMapping("/{id}/invite")
	public ResponseEntity<?> inviteUser(@PathVariable Long id, @RequestBody InviteRequestDto requestDto,
	                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
		try {
			log.info("Controller - inviteUser 메서드 진입");
			ApiResponseDto result = boardService.inviteUser(id, requestDto, userDetails.getUser());
			log.info("Controller - inviteUser 메서드 정상 처리");
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (IllegalArgumentException e) {
			log.info("Controller - inviteUser 메서드 에러 처리");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
		}
	}

	@GetMapping("/{id}/invite")
	public List<InviteResponseDto> getInviteUserList(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		log.info("Controller - getInviteUserList");
		return boardService.getInviteUserList(id);
	}
}