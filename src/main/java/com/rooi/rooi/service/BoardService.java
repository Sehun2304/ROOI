package com.rooi.rooi.service;

import com.rooi.rooi.dto.*;
import com.rooi.rooi.entity.Board;
import com.rooi.rooi.entity.User;

import java.util.List;

public interface BoardService {

	/**
	 * 내가 권한을 가진 모든 보드 가져오기
	 * @param user
	 * @return
	 */
	List<BoardResponseDto> getAllMyBoards(User user);

	/**
	 * 내가 권한을 가진 보드 id 값으로 가져오기
	 * @param id
	 * @return
	 */
	BoardResponseDto getBoardById(Long id);

	/**
	 * 보드 생성
	 * @param requestDto 보드 정보(제목, 설명, 색상)
	 * @param user 보드 생성 요청자
	 * @return 생성된 보드 정보 반환(아이디, 제목, 설명, 색상)
	 */
	BoardResponseDto createBoard(BoardRequestDto requestDto, User user);

	/**
	 * 보드 수정
	 * @param id 수정할 보드 id
	 * @param requestDto 수정할 내용(제목, 설명, 색상)
	 * @param user 보드 수정 요청자
	 * @return 생성된 보드 정보 반환(아이디, 제목, 설명, 색상)
	 */
	BoardResponseDto updateBoard(Long id, BoardRequestDto requestDto, User user);

	/**
	 * 보드 삭제
	 * @param id 삭제할 보드 id
	 * @param user 보드 삭제 요청자
	 * @return 요청 처리 메세지 + 상태코드
	 */
	ApiResponseDto deleteBoard(Long id, User user);

	/**
	 * 유저 초대(권한 부여)
	 * @param id 권한을 추가할 보드 id
	 * @param requestDto 추가할 유저 정보
	 * @param user 초대 요청자
	 * @return 요청 처리 메세지 + 상태코드
	 */
	ApiResponseDto inviteUser(Long id, InviteRequestDto requestDto, User user);

	/**
	 * 보드가 존재하는지 검증
	 * @param id 검증할 보드 id
	 * @return id로 찾은 보드 정보
	 */
	Board findBoard(Long id);

	/**
	 * 해당 유저가 보드 관리 권한이 있는지 검증
	 * @param boardId 검증할 보드 id
	 * @param username 검증할 유저
	 * @return 권한이 있다면 true
	 */
	boolean isPermission(Long boardId, String username);

	/**
	 * 권한이 있는 유저 리스트 가져오기
	 * @param id 보드 id
	 * @return 권한 유저 리스트
	 */
	List<InviteResponseDto> getInviteUserList(Long id);
}