package com.rooi.rooi.service;

import com.rooi.rooi.dto.CardRequestDto;
import com.rooi.rooi.dto.CardResponseDto;
import com.rooi.rooi.dto.ColumnsRequestDto;
import com.rooi.rooi.dto.WorkerRequestDto;
import com.rooi.rooi.entity.Card;
import com.rooi.rooi.entity.Columns;
import com.rooi.rooi.entity.User;
import com.rooi.rooi.entity.Worker;
import com.rooi.rooi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardService {
    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final ColumnsRepository columnsRepository;
    private final PermissionRepository permissionRepository;
    private final WorkerRepository workerRepository;

    // 카드 생성 API
    public CardResponseDto createCard(Long columnId, CardRequestDto cardRequestDto) {
        Columns columns = columnsRepository.findById(columnId).orElseThrow(() -> new NullPointerException("Could Not Found Column"));

        Card card = new Card(columns, cardRequestDto);
        cardRepository.save(card);

        return new CardResponseDto(card);
    }



    // 카드 수정 API
    public void updateCard(Long id, CardRequestDto cardRequestDto) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new NullPointerException("Could Not Found Card"));
        card.update(cardRequestDto);

        cardRepository.save(card);
    }

    // 카드 삭제 API
    public void deleteCard(Long id) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new NullPointerException("Could Not Found Card"));
        cardRepository.delete(card);
    }


    // 작업자 추가 API
    public void addWorker(Long cardId, WorkerRequestDto workerRequestDto) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new NullPointerException("해당 카드가 존재하지 않습니다."));

        // 해당 작업자가 보드에 초대되어 있는지 조회
        User newWorker = checkWorker(workerRequestDto);

        // 요청된 유저가 이미 작업자인지 조회
        Optional<Worker> alreadyWorker = workerRepository.findAllByUserId(newWorker.getId());
        if(alreadyWorker.isPresent()) {
            throw new IllegalArgumentException("이미 작업자로 추가된 유저입니다.");
        }

        // 해당 유저를 작업자로 추가
        Worker worker = new Worker(card, newWorker);
        workerRepository.save(worker);

    }

    // 작업자 삭제 API
    public void deleteWorker(Long cardId, WorkerRequestDto workerRequestDto) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new NullPointerException("해당 카드가 존재하지 않습니다."));

        // 해당 작업자가 보드에 초대되어 있는지 조회
        User newWorker = checkWorker(workerRequestDto);

        // 요청된 유저가 아직 작업자가 아닌지 조회
        Optional<Worker> alreadyWorker = workerRepository.findAllByUserId(newWorker.getId());
        if(alreadyWorker.isEmpty()) {
            throw new IllegalArgumentException("아직 작업자로 추가된 유저가 아닙니다.");
        }

        // 해당 작업자 삭제
        Worker worker = new Worker(card, newWorker);
        workerRepository.delete(worker);
    }

    private User checkWorker(WorkerRequestDto workerRequestDto) {
        User newWorker = userRepository.findByUsername(workerRequestDto.getWorker()).orElseThrow(() -> new NullPointerException("존재하지 않은 사용자 입니다."));
        permissionRepository.findById(newWorker.getId()).orElseThrow(() -> new NullPointerException("초대되지 않은 사용자입니다."));

        return newWorker;
    }

    // 카드>컬럼 이동
    public void moveCard(Long id, Long columnId) {
        Columns columns = columnsRepository.findById(columnId).orElseThrow(() -> new NullPointerException("Could Not Found Column"));
        Card card = cardRepository.findById(id).orElseThrow(() -> new NullPointerException("Could Not Found Card"));
        card.moveCard(columns);
        cardRepository.save(card);
    }
}
