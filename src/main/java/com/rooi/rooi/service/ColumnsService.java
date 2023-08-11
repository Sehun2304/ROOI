package com.rooi.rooi.service;

import com.rooi.rooi.dto.ColumnsRequestDto;
import com.rooi.rooi.dto.ColumnsResponseDto;
import com.rooi.rooi.entity.Board;
import com.rooi.rooi.entity.Columns;
import com.rooi.rooi.entity.User;
import com.rooi.rooi.repository.BoardRepository;
import com.rooi.rooi.repository.ColumnsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ColumnsService {

    private final ColumnsRepository columnsRepository;
    private final BoardRepository boardRepository;

    // 해당 보드의 컬럼 전체 조회 API
    public List<ColumnsResponseDto> getAllColumns(Long boardId) {
        return columnsRepository.findAllByBoardId(boardId).stream()
                .map(ColumnsResponseDto::new)
                .collect(Collectors.toList());
    }

    public ColumnsResponseDto getColumns(Long columnsId) {
        Columns columns = columnsRepository.findById(columnsId).orElseThrow(null);

        if(columns != null) {
            return new ColumnsResponseDto(columns);
        }else {
            return null;}
    }

    public ColumnsResponseDto createColumns(ColumnsRequestDto requestDto, User user){
        Board board = boardRepository.findById(requestDto.getBoardId()).get();

        Columns columns = new Columns(board,requestDto.getTitle());
        columns.setUser(user);

        columnsRepository.save(columns);
        return new ColumnsResponseDto(columns);

    }


    public ColumnsResponseDto updateColumns(Long columnsId, ColumnsRequestDto requestDto, User user){

        Columns columns = columnsRepository.findById(columnsId).orElseThrow(()-> new IllegalArgumentException("컬럼을 찾을 수 없습니다"));
        columns.setTitle(requestDto.getTitle());
        columnsRepository.save(columns);
        return new ColumnsResponseDto(columns);

    }


    public void deleteColumns(Long columnsId, User user){
        Columns columns = columnsRepository.findById(columnsId).orElseThrow(()-> new IllegalArgumentException("컬럼을 찾을 수 없습니다"));
        columnsRepository.delete(columns);
    }


}

