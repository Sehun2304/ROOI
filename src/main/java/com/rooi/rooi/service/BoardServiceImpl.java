package com.rooi.rooi.service;

import com.rooi.rooi.dto.*;
import com.rooi.rooi.entity.Board;
import com.rooi.rooi.entity.Permission;
import com.rooi.rooi.entity.User;
import com.rooi.rooi.repository.BoardRepository;
import com.rooi.rooi.repository.PermissionRepository;
import com.rooi.rooi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

	private final BoardRepository boardRepository;
	private final PermissionRepository permissionRepository;
	private final UserRepository userRepository;

	@Override
	public List<BoardResponseDto> getAllMyBoards(User user) {
		log.info("Service - getAllMyBoards");
		List<Permission> permissionList = permissionRepository.findAllByUserId(user.getId());
		List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();

		for (Permission p : permissionList) {
			Board board = boardRepository.findById(p.getBoard().getId()).orElseThrow(
					() -> new IllegalArgumentException("존재하지 않는 보드입니다.")
			);
			boardResponseDtoList.add(new BoardResponseDto(board));
		}
		return boardResponseDtoList;
	}

	@Override
	public BoardResponseDto getBoardById(Long id) {
		Board board = findBoard(id);
		return new BoardResponseDto(board);
	}

	@Override
	public BoardResponseDto createBoard(BoardRequestDto requestDto, User user) {
		log.info("boardService - createBoard");
		Board board = boardRepository.save(new Board(requestDto, user));
		permissionRepository.save(new Permission(board, user));
		return new BoardResponseDto(board);
	}

	@Override
	@Transactional
	public BoardResponseDto updateBoard(Long id, BoardRequestDto requestDto, User user) {
		// 존재 여부 확인
		Board board = findBoard(id);

		log.info("Service - 유저 검증");
		if (isPermission(id, user.getUsername())) {
			board.setTitle(requestDto.getTitle());
			board.setContents(requestDto.getContents());
			if (!(requestDto.getColor() == null)) {
				board.setColor(requestDto.getColor());
			}
			log.info("Service - 유저 검증 성공");
		} else {
			log.info("Service - 유저 검증 실패");
			throw new IllegalArgumentException("작성자와 관리자만 수정할 수 있습니다.");
		}
		return new BoardResponseDto(board);
	}

	@Override
	public ApiResponseDto deleteBoard(Long id, User user) {
		// 존재 여부 확인
		Board board = findBoard(id);

		log.info("Service - 유저 검증");
		if (board.getUser().getId().equals(user.getId())) {
			boardRepository.delete(board);
			log.info("Service - 유저 검증 성공");
		} else {
			log.info("Service - 유저 검증 실패");
			throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
		}
		return new ApiResponseDto("보드 삭제 완료", HttpStatus.OK.value());
	}

	@Override
	public ApiResponseDto inviteUser(Long id, InviteRequestDto requestDto, User user) {
		log.info("Service - inviteUser 메서드 진입");
		Board board = findBoard(id);
		log.info("Service - findBoard 메서드 : " + board);

		log.info("Service - 작성자 확인");
		if (!(board.getUser().getId().equals(user.getId()))) {
			throw new IllegalArgumentException("작성자만 초대할 수 있습니다.");
		}

		log.info("Service - 유저 확인 전");
		User invitedUser = userRepository.findByUsername(requestDto.getInviteUser()).orElseThrow(
				() -> new IllegalArgumentException("존재하지 않는 사용자입니다.")
		);
		log.info("invitedUser : " + invitedUser);

		if (permissionRepository.existsByBoardIdAndUserId(board.getId(), invitedUser.getId())) {
			throw new IllegalArgumentException("이미 초대받은 사용자입니다.");
		}

		log.info("Service - 유저 확인 후 : " + invitedUser);

		List<Permission> permissionList = permissionRepository.findAllByBoardId(board.getId());
		log.info("Service - permissionList : " + permissionList);

		permissionRepository.save(new Permission(board, invitedUser));

		return new ApiResponseDto("초대 완료", HttpStatus.OK.value());
	}

	@Override
	public Board findBoard(Long id) {
		return boardRepository.findById(id).orElseThrow(
				() -> new IllegalArgumentException("존재하지 않는 보드입니다.")
		);
	}

	@Override
	public boolean isPermission(Long boardId, String username) {
		List<Permission> boardPermissionLIst = permissionRepository.findAllByBoardId(boardId);

		for (Permission p : boardPermissionLIst) {
			if (p.getUser().getUsername().equals(username)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<InviteResponseDto> getInviteUserList(Long id) {
		List<Permission> boardPermissionList = permissionRepository.findAllByBoardId(id);
		List<InviteResponseDto> inviteResponseDtoList = new ArrayList<>();

		for (Permission p : boardPermissionList) {
			inviteResponseDtoList.add(new InviteResponseDto(p));
		}
		return inviteResponseDtoList;
	}
}