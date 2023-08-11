package com.rooi.rooi.dto;

import com.rooi.rooi.entity.Card;
import com.rooi.rooi.entity.Columns;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ColumnsResponseDto extends ApiResponseDto {
    private Long id;
    private String title;
    private List<CardResponseDto> cardList;

    public ColumnsResponseDto(Columns columns){
        this.id = columns.getId();
        this.title = columns.getTitle();
        this.cardList = columns.getCardList().stream()
                .map(CardResponseDto::new)
                .collect(Collectors.toList());
    }

}
