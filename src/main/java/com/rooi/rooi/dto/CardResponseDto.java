package com.rooi.rooi.dto;

import com.rooi.rooi.entity.Card;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CardResponseDto {
    private Long id;
    private String title;
    private String description;
    private Long columnId;
    private LocalDate deadLine;


    public CardResponseDto(Card card) {
        this.id = card.getId();
        this.title = card.getTitle();
        this.description = card.getDescription();
        this.columnId = card.getColumns().getId();
        this.deadLine = card.getDeadLine();
    }
}
