package com.rooi.rooi.controller;

import com.rooi.rooi.dto.ApiResponseDto;
import com.rooi.rooi.dto.ColumnsRequestDto;
import com.rooi.rooi.dto.ColumnsResponseDto;
import com.rooi.rooi.security.UserDetailsImpl;
import com.rooi.rooi.service.ColumnsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ColumnsController {

    private final ColumnsService columnsService;

    // 모든 컬럼 조회 API
    @GetMapping("/all/{boardId}")
    public List<ColumnsResponseDto> getPosts(@PathVariable Long boardId){
        return columnsService.getAllColumns(boardId);
    }

    // 컬럼 조회 API
    @GetMapping("/columns/{columnsId}")
    public ResponseEntity<ColumnsResponseDto> getColumns(@PathVariable Long columnsId) {
        ColumnsResponseDto columnsResponseDto = columnsService.getColumns(columnsId);
        if (columnsResponseDto != null) {
            return ResponseEntity.ok(columnsResponseDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 보드에서 모든 칼럼 조회
    @GetMapping("/board/{boardId}/columns")
    public ResponseEntity<List<ColumnsResponseDto>> getColumnsByBoard(@PathVariable Long boardId) {
        List<ColumnsResponseDto> columnsResponseDtos = columnsService.getColumnsByBoardId(boardId);
        if (columnsResponseDtos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(columnsResponseDtos);
    }

    @PostMapping("/board/{boardId}/columns/{columsid}")
    public ColumnsResponseDto createColumns (@RequestBody ColumnsRequestDto columnsRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return columnsService.createColumns(columnsRequestDto,userDetails.getUser());
    }

    @PutMapping("/columns/{columnsId}")
    public ColumnsResponseDto updateColumns (@PathVariable Long columnsId,@RequestBody ColumnsRequestDto columnsRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return columnsService.updateColumns(columnsId, columnsRequestDto,userDetails.getUser());
    }

    @DeleteMapping("/columns/{columnsId}")
    public ResponseEntity<ApiResponseDto> deleteColumns(@PathVariable Long columnsId,@AuthenticationPrincipal UserDetailsImpl userDetails){
        try{
            columnsService.deleteColumns(columnsId, userDetails.getUser());
        } catch (IllegalArgumentException e){
            return ResponseEntity.ok().body(new ApiResponseDto("컬럼을 찾을 수 없습니다", HttpStatus.BAD_REQUEST.value()));
        }

    return ResponseEntity.ok().body(new ApiResponseDto("컬럼 삭제 성공", HttpStatus.OK.value()));
}
}
