package com.rooi.rooi.controller;

import com.rooi.rooi.dto.ApiResponseDto;
import com.rooi.rooi.dto.CardRequestDto;
import com.rooi.rooi.dto.CardResponseDto;
import com.rooi.rooi.dto.WorkerRequestDto;
import com.rooi.rooi.security.UserDetailsImpl;
import com.rooi.rooi.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    // 카드 생성 API
    @PostMapping("/{columnId}/card")
    public ResponseEntity<CardResponseDto> createCard(@PathVariable Long columnId, @RequestBody CardRequestDto cardRequestDto) {
        CardResponseDto cardResponseDto = cardService.createCard(columnId, cardRequestDto);
        return new ResponseEntity<>(
                cardResponseDto,
                HttpStatus.CREATED
        );
    }

    // 카드 조회 -> 컬럼에서 조회


    // 카드 수정 API
    @PutMapping("/card/{id}")
    public ResponseEntity<ApiResponseDto> updateCard(@PathVariable Long id, @RequestBody CardRequestDto cardRequestDto) {
        cardService.updateCard(id, cardRequestDto);
        ApiResponseDto apiResponseDto = new ApiResponseDto("카드를 수정했습니다.", HttpStatus.OK.value());
        return new ResponseEntity<>(
                apiResponseDto,
                HttpStatus.OK
        );
    }

    // 카드 삭제 API
    @DeleteMapping("/card/{id}")
    public ResponseEntity<ApiResponseDto> deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
        ApiResponseDto apiResponseDto = new ApiResponseDto("카드를 삭제했습니다.", HttpStatus.OK.value());
        return new ResponseEntity<>(
                apiResponseDto,
                HttpStatus.OK
        );
    }

    // 작업자 추가 API
    @PostMapping("/card/{cardId}/worker")
    public ResponseEntity<ApiResponseDto> addWorker(@PathVariable Long cardId, @RequestBody WorkerRequestDto workerRequestDto) {
        try {
            cardService.addWorker(cardId, workerRequestDto);
        } catch (NullPointerException | IllegalArgumentException e) {
            ApiResponseDto apiResponseDto = new ApiResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(apiResponseDto, HttpStatus.BAD_REQUEST);
        }
        ApiResponseDto apiResponseDto = new ApiResponseDto("작업자를 추가했습니다.", HttpStatus.OK.value());
        return new ResponseEntity<>(
                apiResponseDto,
                HttpStatus.OK
        );
    }

    // 작업자 삭제 API
    @DeleteMapping("/card/{cardId}/worker")
    public ResponseEntity<ApiResponseDto> deleteWorker(@PathVariable Long cardId, @RequestBody WorkerRequestDto workerRequestDto) {
        try {
            cardService.deleteWorker(cardId, workerRequestDto);
        } catch (NullPointerException | IllegalArgumentException e) {
            ApiResponseDto apiResponseDto = new ApiResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(apiResponseDto, HttpStatus.BAD_REQUEST);
        }
        ApiResponseDto apiResponseDto = new ApiResponseDto("작업자를 삭제했습니다.", HttpStatus.OK.value());
        return new ResponseEntity<>(
                apiResponseDto,
                HttpStatus.OK
        );
    }

    //카드 컬럼 이동
   @PutMapping("card/move/{id}/{columnId}")//카드id, 이동컬럼id
    public ResponseEntity<ApiResponseDto> moveCard(@PathVariable Long id, @PathVariable Long columnId){
        cardService.moveCard(id, columnId) ;
       ApiResponseDto apiResponseDto = new ApiResponseDto("카드를 이동했습니다.", HttpStatus.OK.value());
       return new ResponseEntity<>(apiResponseDto,HttpStatus.OK);
    }
}
