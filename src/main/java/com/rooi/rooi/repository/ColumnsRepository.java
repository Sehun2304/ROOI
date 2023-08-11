package com.rooi.rooi.repository;

import com.rooi.rooi.entity.Columns;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ColumnsRepository extends JpaRepository<Columns, Long> {
    List<Columns> findAllByBoardId(Long boardId);
}
